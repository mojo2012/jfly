package io.spotnext.jfly;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.spotnext.jfly.http.Cookie;
import io.spotnext.jfly.http.HttpMethod;
import io.spotnext.jfly.http.websocket.ExceptionMessage;
import io.spotnext.jfly.http.websocket.KeepAliveMessage;
import io.spotnext.jfly.http.websocket.Message;
import io.spotnext.jfly.http.websocket.Message.MessageType;
import io.spotnext.jfly.util.JsonUtil;
import io.spotnext.jfly.util.KeyValueListMapping;
import io.spotnext.jfly.util.KeyValueMapping;
import io.spotnext.jfly.viewhandlers.ExceptionViewHandler;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Service;

@WebSocket
public class Server implements ClientCommunicationHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	private static ThreadLocal<Server> currentInstance = new ThreadLocal<>();

	public static final int DEFAULT_PORT = 8080;
	public static final String DEFAULT_STATIC_FILE_PATH = "/public";
	public static final String DEFAULT_WEBSOCKET_PATH = "/com";

	protected final KeyValueMapping<String, HttpSession> httpSessions = new KeyValueMapping<>();
	protected final KeyValueMapping<String, Session> websocketSessions = new KeyValueMapping<>();
	protected final ThreadLocal<Session> currentWebSocketSession = new ThreadLocal<>();

	protected final KeyValueListMapping<String, ViewHandler> sessionViewHandlers = new KeyValueListMapping<>();
	protected final KeyValueMapping<String, Consumer<ViewHandler>> urlViewHandlerPostProcessorMapping = new KeyValueMapping<>();

	protected List<BiConsumer<HttpSession, Message>> onBeforeMessageReceivedHandlers = new ArrayList<>();
	protected List<BiConsumer<HttpSession, Message>> onAfterMessageReceivedHandlers = new ArrayList<>();

	protected Service service;

	public Server(final int port) {
		this(port, null, null);
	}

	/**
	 * Creates a new jfly ViewHandler.
	 * 
	 * @param port
	 * @param staticFileLocation
	 * @param componentTemplatePath
	 */
	public Server(final int port, BiConsumer<Request, Response> beforeFilter,
			BiConsumer<Request, Response> afterFilter) {

		service = Service.ignite();
		service.port(port);
		service.webSocket(DEFAULT_WEBSOCKET_PATH, this);
		service.staticFileLocation(DEFAULT_STATIC_FILE_PATH);

		if (beforeFilter != null) {
			service.before((req, res) -> beforeFilter.accept(req, res));
		}

		if (afterFilter != null) {
			service.after((req, res) -> afterFilter.accept(req, res));
		}

		// NOT YET WORKING: Service.hasMultipleHandlers() returns wrong value in
		// case a websocket handler is set ...
		// redirect to the error page in case there is a 404 error
		service.notFound((req, res) -> {
			res.status(HttpStatus.NOT_FOUND_404);
			res.body("Page not found");
			return null;
		});

		// redirect to the error page in case there is an internal server error
		service.internalServerError((req, res) -> {
			res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
			// TODO return nice error page
			res.body("An error occurred");

			return null;
		});

		// redirect to the error page in case there is an exception during
		// initial rendering of the view
		service.exception(Exception.class, (ex, req, res) -> {
			res.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
			// TODO return nice error page
			res.body("An error occurred");
		});

		registerViewHandler(Arrays.asList("/error"), ExceptionViewHandler.class);
	}

	private void startScavengerThread() {
		final TimerTask scavenger = new TimerTask() {
			@Override
			public void run() {
				final LocalDateTime threshold = LocalDateTime.now().minus(20l, ChronoUnit.MINUTES);

				for (Map.Entry<String, List<ViewHandler>> handlers : sessionViewHandlers.entrySet()) {
					final List<ViewHandler> expiredHandlers = handlers.getValue().stream() //
							.filter(v -> !(v instanceof SinglePageApplication)) // ignore single page apps, they die with the session
							.filter(v -> v.lastKeepAlive != null && v.lastKeepAlive.isBefore(threshold)) //
							.collect(Collectors.toList());

					for (ViewHandler h : expiredHandlers) {
						sessionViewHandlers.remove(handlers.getKey(), h);
					}
				}

				System.gc();
			}
		};

		new Timer().scheduleAtFixedRate(scavenger, 60 * 1000, 60 * 1000);
	}

	protected ExceptionViewHandler getErrorHandler(Request request) {
		ExceptionViewHandler handler = new ExceptionViewHandler();
		handler.init(createRequest(request), this);
		return handler;
	}

	public Server registerViewHandler(List<String> urls, Class<? extends ViewHandler> handler) {
		return registerViewHandler(urls, handler, null);
	}

	/**
	 * 
	 * @param urls
	 *            the urls to handle
	 * @param handler
	 *            the {@link ViewHandler} class to be bound to the given urls
	 * @param postProcessor
	 *            as first argument the newly created {@link ViewHandler} is
	 *            passed, the second argument is the current session id.
	 * @return
	 */
	public Server registerViewHandler(List<String> urls, Class<? extends ViewHandler> handler,
			BiConsumer<HttpSession, ViewHandler> postProcessor) {

		for (String url : urls) {
			service.get(url, (req, res) -> {
				final String pathInfo = req.raw().getPathInfo();

				if (!pathInfo.equals(DEFAULT_WEBSOCKET_PATH)) {
					httpSessions.put(req.session().id(), req.session().raw());
					return render(handler, postProcessor, req, res);
				}

				return null;
			});
		}

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
		startScavengerThread();
	}

	/*
	 * Websocket listener implementation
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

				final ViewHandler viewHandler = getViewHandler(msg.getSessionId(), msg.getViewId());

				if (viewHandler != null) {
					if (MessageType.keepAlive.equals(msg.getType())) {
						viewHandler.lastKeepAlive = LocalDateTime.now();
						sendMessage(new KeepAliveMessage());
					} else {
						onBeforeMessageReceivedHandlers.stream().forEach(h -> h.accept(getCurrentHttpSession(), msg));

						try {
							viewHandler.handleMessage(msg);
						} catch (Exception e) {
							// send back an error message to allow the UI to
							// react properly
							LOG.error("An error occurred", e);

							sendErrorMessage(null, e);
						} finally {
							onAfterMessageReceivedHandlers.stream()
									.forEach(h -> h.accept(getCurrentHttpSession(), msg));
						}
					}
				} else {
					throw new IOException("No ViewHandler for the given session is found.");
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

	protected String render(Class<? extends ViewHandler> handler, BiConsumer<HttpSession, ViewHandler> postProcessor,
			final Request request, final Response response) throws Exception {

		ViewHandler view = null;

		try {
			view = getOrCreateViewHandler(request, handler, postProcessor);

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

	protected ViewHandler getViewHandler(String sessionId, String viewId) {
		List<ViewHandler> views = sessionViewHandlers.get(sessionId);

		return views.stream().filter(v -> viewId.equals(v.getViewUid())).findFirst().orElse(null);
	}

	protected ViewHandler getOrCreateViewHandler(Request request, Class<? extends ViewHandler> handler,
			BiConsumer<HttpSession, ViewHandler> postProcessor) throws InstantiationException, IllegalAccessException {

		// always use the same instance for single-page apps
		ViewHandler view = SinglePageApplication.class.isAssignableFrom(handler)
				? getViewHandler(request.session().id(), SinglePageApplication.VIEW_ID)
				: null;

		try {
			if (view == null) {
				Constructor<? extends ViewHandler> constructor = handler.getDeclaredConstructor();
				final ViewHandler newViewHandler = (ViewHandler) constructor.newInstance();
				view = newViewHandler;
			}

			if (postProcessor != null) {
				postProcessor.accept(request.session().raw(), view);
			}

			sessionViewHandlers.putOrAdd(request.session().id(), view);
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new InstantiationException(String.format("Could not create view handler for url %s", request.uri()));
		}

		view.init(createRequest(request), this);

		view.onDestroy(viewHandler -> {
			LOG.debug(String.format("Destroyed view %s", request.uri()));

			Optional<ViewHandler> vHandler = sessionViewHandlers.get(viewHandler.getSessionId()).stream() //
					.filter(v -> viewHandler.getViewUid().equals(v.getViewUid())) //
					.findFirst();

			if (vHandler.isPresent()) {
				sessionViewHandlers.remove(viewHandler.getSessionId(), vHandler.get());
			}
		});

		return view;
	}

	protected HttpRequest createRequest(Request request) {
		String sessionId = request.session().id();
		KeyValueMapping<String, Object> sessionAttributes = new KeyValueMapping<>();
		request.session().attributes().forEach(k -> sessionAttributes.put(k, request.session().attribute(k)));
		io.spotnext.jfly.http.HttpSession session = new io.spotnext.jfly.http.HttpSession(sessionId, sessionAttributes);

		HttpRequest req = new HttpRequest(HttpMethod.valueOf(request.requestMethod()), request.uri(), request.params(),
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
	public void onBeforeReceiveMessage(BiConsumer<HttpSession, Message> handler) {
		onBeforeMessageReceivedHandlers.add(handler);
	}

	/**
	 * This will be called after every new message that arrives, regardless if
	 * the message could be processed successfully.
	 * 
	 * @param handler
	 */
	public void onAfterReceiveMessage(BiConsumer<HttpSession, Message> handler) {
		onAfterMessageReceivedHandlers.add(handler);
	}
}
