package at.spot.jfly;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import at.spot.jfly.util.GsonUtil;
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

	protected final Map<String, Application> sessionApplications = new ConcurrentHashMap<>();

	protected final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
	protected final ThreadLocal<Session> context = new ThreadLocal<>();

	protected Class<? extends Application> appClass;

	/**
	 * Creates a new jfly application using the default port
	 * {@link Server#DEFAULT_PORT}.
	 * 
	 * @param port
	 * @param staticFileLocation
	 * @param componentTemplatePath
	 */
	public Server(final Class<? extends Application> appClass) {
		this(appClass, DEFAULT_PORT);
	}

	/**
	 * Creates a new jfly application.
	 * 
	 * @param port
	 * @param staticFileLocation
	 * @param componentTemplatePath
	 */
	public Server(final Class<? extends Application> appClass, final int port) {
		this.appClass = appClass;
		Spark.port(port);
		Spark.webSocket(DEFAULT_WEBSOCKET_PATH, this);
		Spark.staticFileLocation(DEFAULT_STATIC_FILE_PATH);

		Spark.exception(Exception.class, (ex, req, res) -> {
			res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
			res.body("Fatal server error.");
		});

		Spark.get("/", (req, res) -> {
			return render(req, res, null);
		});
	}

	/**
	 * The static file location defines the path under which static files, like
	 * javascripts or css styles sheets are looked for. The path has to be in
	 * the classpath.<br/>
	 * Default: {@link Server#DEFAULT_STATIC_FILE_PATH}
	 */
	public Server staticFileLocation(final String staticFileLocation) {
		Spark.staticFileLocation(staticFileLocation);
		return this;
	}

	/**
	 * This is called before every request is executed. A common usecase is to
	 * check for authentication.
	 * 
	 * @param requestFilter
	 * @return
	 */
	public Server before(final Filter requestFilter) {
		Spark.before(requestFilter);
		return this;
	}

	public void init() {
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
		System.out.println("Got: " + message); // Print message

		setCurrentSession(session);

		if (StringUtils.isNotBlank(message)) {
			final JsonObject msg = GsonUtil.fromJson(message, JsonObject.class);

			final JsonElement sessionId = msg.get("sessionId");
			final JsonElement urlPath = msg.get("urlPath");

			if (sessionId != null && urlPath != null) {
				final Application app = sessionApplications.get(sessionId.getAsString());

				if (app != null) {
					final Object retVal = app.handleMessage(msg, urlPath.getAsString());

					if (retVal != null) {
						sendMessage(retVal);
					}
				} else {
					throw new IOException("No application for the given session is found.");
				}
			}
		}
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
	 * application functionality
	 */

	protected String render(final Request request, final Response response, final Exception ex) throws Exception {
		Application app = sessionApplications.get(request.session().id());

		try {
			if (app == null) {
				app = appClass.newInstance();
				app.init(this, request.session().id());

				sessionApplications.put(request.session().id(), app);
			}

			final Map<String, Object> cookie = new HashMap<>();
			cookie.put("sessionId", app.sessionId());
			response.cookie("jfly", GsonUtil.toJson(cookie));

			return app.render(request.url());
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
			throw new Exception("Cannot instantiace application", e);
		}
	}

	@Override
	public void sendMessage(final Object message) {
		try {
			getCurrentSession().getRemote().sendString(GsonUtil.toJson(message));
		} catch (final Exception e) {
			LOG.trace("Cannot send message to client.");
		}
	}

	@Override
	public void sendMessage(final String clientSessionId, final Object message) {

	}
}
