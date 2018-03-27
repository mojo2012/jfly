package at.spot.jfly;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.spot.jfly.http.Cookie;
import at.spot.jfly.http.HttpMethod;
import at.spot.jfly.http.HttpSession;
import at.spot.jfly.http.websocket.ExceptionMessage;
import at.spot.jfly.http.websocket.KeepAliveMessage;
import at.spot.jfly.http.websocket.Message;
import at.spot.jfly.http.websocket.Message.MessageType;
import at.spot.jfly.util.JsonUtil;
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

	protected final KeyValueMapping<String, Session> sessions = new KeyValueMapping<>();
	protected final ThreadLocal<Session> context = new ThreadLocal<>();

	protected final KeyValueMapping<String, KeyValueMapping<String, ViewHandler>> sessionViewHandlers = new KeyValueMapping<>();
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
			res.status(404);
			res.redirect("/error");
			return null;
		});

		// redirect to the error page in case there is an internal server error
		Spark.internalServerError((req, res) -> {
			res.status(500);
			res.redirect("/error");
			return null;
		});

		// redirect to the error page in case there is an exception during initial
		// rendering of the view
		Spark.exception(Exception.class, (ex, req, res) -> {
			res.status(500);
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
		Optional<Cookie> cookie = session.getUpgradeRequest().getCookies().stream()
				.filter(c -> "jfly".equals(c.getName())).map(c -> decodeCookie(c.getValue())).findFirst();

		addSession(cookie.get().getValue(), session);
		setCurrentSession(session);
	}

	@OnWebSocketClose
	public void closed(final Session session, final int statusCode, final String reason) {
		closeSession(session);
	}

	@OnWebSocketMessage
	public <M extends Message> void message(final Session session, final String message) throws IOException {
		LOG.debug("Received message: " + message);

		try {
			setCurrentSession(session);

			if (StringUtils.isNotBlank(message)) {
				final Message msg = JsonUtil.fromJson(message, Message.class);

				if (MessageType.keepAlive.equals(msg.getType())) {
					sendMessage(new KeepAliveMessage());
				} else {
					final ViewHandler app = getViewHandler(msg.getSessionId(), msg.getUrl());

					if (app != null) {
						try {
							app.handleMessage(msg);
							// final Message retVal = app.handleMessage(msg);
							//
							// if (retVal != null) {
							// sendMessage(retVal);
							// }
						} catch (Exception e) {
							// send back an error message to allow the UI to react properly
							sendMessage(generateErrorMessage(e));
						}
					} else {
						throw new IOException("No ViewHandler for the given session is found.");
					}
				}
			}
		} catch (Throwable e) {
			sendMessage(generateErrorMessage(e));
		}
	}

	protected KeepAliveMessage generateKeepaliveAnswerMessage() {
		KeepAliveMessage message = new KeepAliveMessage();

		return message;
	}

	protected ExceptionMessage generateErrorMessage(Throwable exception) {
		ExceptionMessage msg = new ExceptionMessage();

		msg.setName(exception.getClass().getName());
		msg.setDescription(exception.getMessage());

		return msg;
	}

	protected void addSession(String sessionId, final Session session) {
		sessions.put(sessionId, session);
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

	protected String getWebSocketSessionId(Session session) {
		return ((javax.servlet.http.HttpSession) session.getUpgradeRequest().getSession()).getId();
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
		ViewHandler view = null;

		try {
			view = getOrCreateViewHandler(request);

			Cookie cookie = new Cookie();
			cookie.setName("sessionId");
			cookie.setValue(view.getSessionId());
			response.cookie("jfly", encodeCookie(cookie));
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e);
		}

		if (view == null) {
			throw new IllegalStateException("No view handler found.");
		}

		return view.render();
	}

	protected String encodeCookie(Cookie cookie) {
		return Base64.getEncoder().encodeToString(JsonUtil.toJson(cookie).getBytes());
	}

	protected Cookie decodeCookie(String cookieString) {
		return JsonUtil.fromJson(new String(Base64.getDecoder().decode(cookieString)), Cookie.class);
	}

	protected KeyValueMapping<String, ViewHandler> getViewHandlerMapping(String sessionId) {
		KeyValueMapping<String, ViewHandler> mapping = sessionViewHandlers.get(sessionId);

		if (mapping == null) {
			mapping = new KeyValueMapping<>();
			sessionViewHandlers.put(sessionId, mapping);
		}

		return mapping;
	}

	protected ViewHandler getViewHandler(String sessionId, String url) {
		return getViewHandlerMapping(sessionId).get(url);
	}

	protected ViewHandler getOrCreateViewHandler(Request request)
			throws InstantiationException, IllegalAccessException {

		final KeyValueMapping<String, ViewHandler> mapping = getViewHandlerMapping(request.session().id());
		ViewHandler view = mapping.get(request.uri());

		if (view == null) {
			// get view handler class from registered view handlers mappings and put it into
			// the session mapping object
			view = urlViewHandlerMapping.get(request.uri()).newInstance();
			view.init(createRequest(request), this);

			final String url = request.uri();

			view.onDestroy(() -> {
				mapping.remove(url);

				LOG.debug(String.format("Destroyed view %s", request.uri()));
			});

			mapping.put(request.uri(), view);
		}

		return view;
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
	public <M extends Message> void sendMessage(final M message) {
		sendMessage(message, getCurrentSession().getRemote());
	}

	@Override
	public <M extends Message> void sendMessage(final M message, String sessionId) {
		sendMessage(message, sessions.get(sessionId).getRemote());
	}

	protected <M extends Message> void sendMessage(final M message, RemoteEndpoint endpoint) {
		try {
			String messageString = JsonUtil.toJson(message);
			LOG.debug("Sending message: " + JsonUtil.toJson(message));
			endpoint.sendString(messageString);
		} catch (final Exception e) {
			LOG.error("Cannot send message to client.");
		}
	}
}
