package at.spot.jfly;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.servlet.http.HttpSession;

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
import spark.Service;

@WebSocket
public class Server implements ClientCommunicationHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	private static ThreadLocal<Server> currentInstance = new ThreadLocal<>();

	public static final int DEFAULT_PORT = 8080;
	public static final String DEFAULT_STATIC_FILE_PATH = "/web";
	public static final String DEFAULT_WEBSOCKET_PATH = "/com";

	protected final KeyValueMapping<String, HttpSession> httpSessions = new KeyValueMapping<>();
	protected final KeyValueMapping<String, Session> websocketSessions = new KeyValueMapping<>();
	protected final ThreadLocal<Session> currentWebSocketSession = new ThreadLocal<>();

	protected final KeyValueMapping<String, KeyValueMapping<String, ViewHandler>> sessionViewHandlers = new KeyValueMapping<>();
	protected final KeyValueMapping<String, Class<? extends ViewHandler>> urlViewHandlerMapping = new KeyValueMapping<>();
	protected final KeyValueMapping<String, Consumer<ViewHandler>> urlViewHandlerPostProcessorMapping = new KeyValueMapping<>();

	protected List<BiConsumer<HttpSession, Message>> onMessageReceivedHandlers = new ArrayList<>();

	protected Service service;

	/**
	 * Creates a new jfly ViewHandler.
	 * 
	 * @param port
	 * @param staticFileLocation
	 * @param componentTemplatePath
	 */
	public Server(final int port) {
		service = Service.ignite();
		service.port(port);
		service.webSocket(DEFAULT_WEBSOCKET_PATH, this);
		service.staticFileLocation(DEFAULT_STATIC_FILE_PATH);

		// NOT YET WORKING: Service.hasMultipleHandlers() returns wrong value in
		// case a
		// websocket handler is set ...
		// redirect to the error page in case there is a 404 error
		service.notFound((req, res) -> {
			res.status(404);
			res.redirect("/error");
			return null;
		});

		// redirect to the error page in case there is an internal server error
		service.internalServerError((req, res) -> {
			res.status(500);
			res.redirect("/error");
			return null;
		});

		// redirect to the error page in case there is an exception during
		// initial
		// rendering of the view
		service.exception(Exception.class, (ex, req, res) -> {
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
		return registerViewHandler(url, handler, null);
	}

	public Server registerViewHandler(String url, Class<? extends ViewHandler> handler,
			Consumer<ViewHandler> postProcessor) {

		urlViewHandlerMapping.put(url, handler);

		if (postProcessor != null) {
			urlViewHandlerPostProcessorMapping.put(url, postProcessor);
		}

		service.get(url, (req, res) -> {
			httpSessions.put(req.session().id(), req.session().raw());
			return render(req, res);
		});

		return this;
	}

	/**
	 * The static file location defines the path under which static files, like
	 * javascripts or CSS stylesheets are looked for. The path has to be in the
	 * classpath.<br/>
	 * Default: {@link Server#DEFAULT_STATIC_FILE_PATH}
	 */
	public Server staticFileLocation(final String staticFileLocation) {
		service.staticFileLocation(staticFileLocation);
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
		service.before(requestFilter);
		return this;
	}

	public void start() {
		service.init();
	}

	/*
	 * Websocket listener implementaito
	 */

	@OnWebSocketConnect
	public void connected(final Session session) {
		addSession(session);
		setCurrentWebSocketSession(session);
	}

	@OnWebSocketClose
	public void closed(final Session session, final int statusCode, final String reason) {
		closeSession(session);
	}

	protected String getSessionId(Session session) {
		final Optional<Cookie> cookie = getCookie(session);
		return cookie.get().getValue();
	}

	protected Optional<Cookie> getCookie(Session session) {
		return session.getUpgradeRequest().getCookies().stream().filter(c -> "jfly".equals(c.getName()))
				.map(c -> decodeCookie(c.getValue())).findFirst();
	}

	protected org.eclipse.jetty.server.session.Session getWebSession(Session websocketSession) {
		return (org.eclipse.jetty.server.session.Session) websocketSession.getUpgradeRequest().getSession();
	}

	@Override
	public HttpSession getCurrentHttpSession() {
		return httpSessions.get(getSessionId(getCurrentWebSocketSession()));
	}

	@OnWebSocketMessage
	public <M extends Message> void message(final Session session, final String message) throws IOException {
		LOG.debug("Received message: " + message);

		try {
			setCurrentInstance();
			setCurrentWebSocketSession(session);

			if (StringUtils.isNotBlank(message)) {
				final Message msg = JsonUtil.fromJson(message, Message.class);

				onMessageReceivedHandlers.stream().forEach(h -> h.accept(getCurrentHttpSession(), msg));

				if (MessageType.keepAlive.equals(msg.getType())) {
					sendMessage(new KeepAliveMessage());
				} else {
					final ViewHandler app = getViewHandler(msg.getSessionId(), msg.getUrl());

					if (app != null) {
						try {
							app.handleMessage(msg);
						} catch (Exception e) {
							// send back an error message to allow the UI to
							// react properly
							LOG.error("An error occurred", e);
							;
							sendErrorMessage(null, e);
						}
					} else {
						throw new IOException("No ViewHandler for the given session is found.");
					}
				}
			}
		} catch (Throwable e) {
			LOG.error("Could not process message", e);
			sendErrorMessage(null, e);
		}
	}

	protected void setCurrentInstance() {
		currentInstance.set(this);
	}

	public static Server getCurrentInstance() {
		return currentInstance.get();
	}

	protected KeepAliveMessage generateKeepaliveAnswerMessage() {
		KeepAliveMessage message = new KeepAliveMessage();

		return message;
	}

	@Override
	public void sendErrorMessage(String message, Throwable exception) {
		sendMessage(generateErrorMessage(message, exception));
	}

	protected ExceptionMessage generateErrorMessage(String message, Throwable exception) {
		ExceptionMessage msg = new ExceptionMessage();

		String exceptionName = exception != null ? exception.getClass().getName() : "unknown";
		String description = StringUtils.isNotBlank(message) ? message
				: (exception != null && exception.getMessage() != null) ? exception.getMessage()
						: "An exception of type '" + exceptionName + "' has occurred.";

		msg.setName(exceptionName);
		msg.setDescription(description);

		return msg;
	}

	protected void addSession(final Session session) {
		websocketSessions.put(getSessionId(session), session);
	}

	protected void closeSession(Session session) {
		if (session != null) {
			websocketSessions.remove(getSessionId(session));

			try {
				session.disconnect();
			} catch (final IOException e) {
				e.printStackTrace();
			}

			session.close();
		}
	}

	public void closeCurrentSession() {
		closeSession(getCurrentWebSocketSession());
	}

	protected void setCurrentWebSocketSession(final Session session) {
		currentWebSocketSession.set(session);
	}

	protected String getWebSocketSessionId(Session session) {
		return ((javax.servlet.http.HttpSession) session.getUpgradeRequest().getSession()).getId();
	}

	public boolean isCalledInRequest() {
		return getCurrentWebSocketSession() != null;
	}

	protected Session getCurrentWebSocketSession() {
		return currentWebSocketSession.get();
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
			// get view handler class from registered view handlers mappings and
			// put it into the session mapping object

			try {
				Constructor<? extends ViewHandler> constructor = urlViewHandlerMapping.get(request.uri())
						.getDeclaredConstructor();
				final ViewHandler newViewHandler = (ViewHandler) constructor.newInstance();
				view = newViewHandler;

				urlViewHandlerPostProcessorMapping.apply(request.uri(), i -> i.accept(newViewHandler));
			} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {

				throw new InstantiationException(
						String.format("Could not create view handler for url %s", request.uri()));
			}

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
		at.spot.jfly.http.HttpSession session = new at.spot.jfly.http.HttpSession(sessionId, sessionAttributes);

		HttpRequest req = new HttpRequest(HttpMethod.valueOf(request.requestMethod()), request.params(),
				request.cookies(), session);

		return req;
	}

	@Override
	public <M extends Message> void sendMessage(final M message) {
		sendMessage(message, getCurrentWebSocketSession().getRemote());
	}

	@Override
	public <M extends Message> void sendMessage(final M message, String sessionId) {
		sendMessage(message, websocketSessions.get(sessionId).getRemote());
	}

	protected <M extends Message> void sendMessage(final M message, RemoteEndpoint endpoint) {
		try {
			String messageString = JsonUtil.toJson(message);
			LOG.debug("Sending message: " + JsonUtil.toJson(message));
			endpoint.sendString(messageString);
		} catch (final Exception e) {
			LOG.error("Cannot send message to client.", e);
		}
	}

	// events
	public void onReceiveMessage(BiConsumer<HttpSession, Message> handler) {
		onMessageReceivedHandlers.add(handler);
	}
}
