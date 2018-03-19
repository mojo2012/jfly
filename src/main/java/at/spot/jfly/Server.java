package at.spot.jfly;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import at.spot.jfly.http.HttpMethod;
import at.spot.jfly.http.HttpSession;
import at.spot.jfly.util.GsonUtil;
import at.spot.jfly.util.KeyValueMapping;
import at.spot.jfly.viewhandlers.ExceptionViewHandler;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

@WebSocket
public class Server implements ClientCommunicationHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	public static final int DEFAULT_PORT = 8080;
	public static final String DEFAULT_STATIC_FILE_PATH = "/web";
	public static final String DEFAULT_WEBSOCKET_PATH = "/com";

	protected final Map<String, ViewHandler> sessionViewHandlers = new ConcurrentHashMap<>();

	protected final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
	protected final ThreadLocal<Session> context = new ThreadLocal<>();

	protected final KeyValueMapping<String, Class<? extends ViewHandler>> urlViewHandlerMapping = new KeyValueMapping<>();

	/**
	 * Creates a new jfly ViewHandler.
	 * 
	 * @param port
	 * @param staticFileLocation
	 * @param componentTemplatePath
	 */
	public Server(final int port) {
		Spark.port(port);
		Spark.webSocket(DEFAULT_WEBSOCKET_PATH, this);
		Spark.staticFileLocation(DEFAULT_STATIC_FILE_PATH);

		// NOT YET WORKING: Service.hasMultipleHandlers() returns wrong value in case a
		// websocket handler is set ...
		// redirect to the error page in case there is a 404 error
		Spark.notFound((req, res) -> {
			res.redirect("/error");
			return null;
		});

		// redirect to the error page in case there is an internal server error
		Spark.internalServerError((req, res) -> {
			res.redirect("/error");
			return null;
		});

		// redirect to the error page in case there is an exception during initial
		// rendering of the view
		Spark.exception(Exception.class, (ex, req, res) -> {
			res.redirect("/error");
		});

		registerViewHandler("/error", ExceptionViewHandler.class);
	}

	protected ExceptionViewHandler getErrorHandler(Request request) {
		ExceptionViewHandler handler = new ExceptionViewHandler();
		handler.init(createRequest(request), this);
		return handler;
	}

	public Server registerViewHandler(String url, Class<? extends ViewHandler> handler) {
		urlViewHandlerMapping.put(url, handler);

		Spark.get(url, (req, res) -> {
			return render(req, res);
		});

		return this;
	}

	/**
	 * The static file location defines the path under which static files, like
	 * javascripts or css styles sheets are looked for. The path has to be in the
	 * classpath.<br/>
	 * Default: {@link Server#DEFAULT_STATIC_FILE_PATH}
	 */
	public Server staticFileLocation(final String staticFileLocation) {
		Spark.staticFileLocation(staticFileLocation);
		return this;
	}

	/**
	 * This is called before every request is executed. A common usecase is to check
	 * for authentication.
	 * 
	 * @param requestFilter
	 * @return
	 */
	public Server before(final Filter requestFilter) {
		Spark.before(requestFilter);
		return this;
	}

	public void start() {
		Spark.init();
	}

	/*
	 * Websocket listener implementaito
	 */

	@OnWebSocketConnect
	public void connected(final Session session) {
		addSession(session);
		setCurrentSession(session);
	}

	@OnWebSocketClose
	public void closed(final Session session, final int statusCode, final String reason) {
		closeSession(session);
	}

	@OnWebSocketMessage
	public void message(final Session session, final String message) throws IOException {
		LOG.debug("Received message: " + message);

		setCurrentSession(session);

		if (StringUtils.isNotBlank(message)) {
			final JsonObject msg = GsonUtil.fromJson(message, JsonObject.class);

			final JsonElement sessionId = msg.get("sessionId");
			final JsonElement urlPath = msg.get("urlPath");

			if (sessionId != null && urlPath != null) {
				final ViewHandler app = sessionViewHandlers.get(sessionId.getAsString());

				if (app != null) {
					try {
						final Object retVal = app.handleMessage(msg, urlPath.getAsString());

						if (retVal != null) {
							sendMessage(retVal);
						}
					} catch (Exception e) {
						// send back an error message to allow the UI to react properly
						sendMessage(generateErrorMessage(e));
					}
				} else {
					throw new IOException("No ViewHandler for the given session is found.");
				}
			}
		}
	}

	protected Map<String, Object> generateErrorMessage(Exception exception) {
		Map<String, Object> ret = new HashMap<>();

		ret.put("type", "exception");
		ret.put("description", exception.getMessage());
		// ret.put("stackTrace", ExceptionUtils.getStackTrace(exception));

		return ret;
	}

	protected void addSession(final Session session) {
		sessions.add(session);
	}

	protected void closeSession(final Session session) {
		if (session != null) {
			try {
				session.disconnect();
			} catch (final IOException e) {
				e.printStackTrace();
			}

			session.close();
			sessions.remove(session);
		}
	}

	public void closeCurrentSession() {
		closeSession(getCurrentSession());
	}

	protected void setCurrentSession(final Session session) {
		context.set(session);
	}

	public boolean isCalledInRequest() {
		return getCurrentSession() != null;
	}

	protected Session getCurrentSession() {
		return context.get();
	}

	/*
	 * ViewHandler functionality
	 */

	protected String render(final Request request, final Response response) throws Exception {
		ViewHandler app = null;
		HttpRequest httpRequest = createRequest(request);
		sessionViewHandlers.get(request.session().id());

		try {
			if (app == null) {
				app = urlViewHandlerMapping.get(request.uri()).newInstance();
				app.init(httpRequest, this);
				app.onDestroy(() -> {
					sessionViewHandlers.remove(request.session().id());
				});

				sessionViewHandlers.put(request.session().id(), app);
			}

			final Map<String, Object> cookie = new HashMap<>();
			cookie.put("sessionId", app.getSessionId());
			response.cookie("jfly", Base64.getEncoder().encodeToString(GsonUtil.toJson(cookie).getBytes()));
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
			throw new Exception("Cannot instantiate ViewHandler", e);
		}

		return app.render();
	}

	protected HttpRequest createRequest(Request request) {
		String sessionId = request.session().id();
		KeyValueMapping<String, Object> sessionAttributes = new KeyValueMapping<>();
		request.session().attributes().forEach(k -> sessionAttributes.put(k, request.session().attribute(k)));
		HttpSession session = new HttpSession(sessionId, sessionAttributes);

		HttpRequest req = new HttpRequest(HttpMethod.valueOf(request.requestMethod()), request.params(),
				request.cookies(), session);

		return req;
	}

	@Override
	public void sendMessage(final Object message) {
		try {
			String messageString = GsonUtil.toJson(message);
			LOG.debug("Sending message: " + GsonUtil.toJson(message));
			getCurrentSession().getRemote().sendString(messageString);
		} catch (final Exception e) {
			LOG.error("Cannot send message to client.");
		}
	}

	@Override
	public void sendMessage(final String clientSessionId, final Object message) {

	}
}
