package at.spot.jfly;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.spot.jfly.event.Event;
import at.spot.jfly.event.Events;
import at.spot.jfly.event.Events.EventType;
import at.spot.jfly.event.Events.GenericEvent;
import at.spot.jfly.http.websocket.ComponentManipulationMessage;
import at.spot.jfly.http.websocket.ComponentStateUpdateMessage;
import at.spot.jfly.http.websocket.EventMessage;
import at.spot.jfly.http.websocket.FunctionCallMessage;
import at.spot.jfly.http.websocket.IntialStateUpdateMessage;
import at.spot.jfly.http.websocket.Message;
import at.spot.jfly.http.websocket.Message.MessageType;
import at.spot.jfly.templating.ComponentContext;
import at.spot.jfly.templating.Template;
import at.spot.jfly.templating.TemplateService;
import at.spot.jfly.templating.impl.VelocityTemplateService;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.ClientUpdateCommand;
import at.spot.jfly.ui.base.Component;
import at.spot.jfly.ui.base.EventTarget;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.html.Html;
import at.spot.jfly.util.ObjectUtils;

public abstract class ViewHandler implements ComponentHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ViewHandler.class);

	public static final String DEFAULT_COMPONENT_TEMPLATE_PATH = "/template/component";

	protected final Map<String, Component> registeredComponents = new ConcurrentHashMap<>();
	protected TemplateService templateService;
	protected ClientCommunicationHandler clientCommunicationHandler;

	protected String componentTemplatePath;
	protected String sessionId;

	private Html html;
	private boolean formatOutput = true;
	private Locale currentLocale = Locale.getDefault();

	protected final List<Runnable> onDestroyEventListener = new ArrayList<>();

	public void init(HttpRequest request, final ClientCommunicationHandler handler) {
		setSessionId(request.getSession().getId());
		setClientCommunicationHandler(handler);

		if (StringUtils.isBlank(componentTemplatePath)) {
			this.componentTemplatePath = DEFAULT_COMPONENT_TEMPLATE_PATH;
		}

		if (this.templateService == null) {
			this.templateService = new VelocityTemplateService(componentTemplatePath);
		}

		if (clientCommunicationHandler == null) {
			throw new RuntimeException("No client communication handler set");
		}

		html = new Html(this);
		html.setHead(createHeader());
		html.setBody(createBody());
	}

	/**
	 * Defines the path under which the velocity template files for rendering
	 * the UI components in the browser is looked for. The path has to be in the
	 * classpath.<br />
	 * Default: {@link Server#DEFAULT_COMPONENT_TEMPLATE_PATH}
	 */
	public void setComponentTemplatePath(final String path) {
		this.componentTemplatePath = path;
	}

	public void setClientCommunicationHandler(final ClientCommunicationHandler clientCommunicationHandler) {
		this.clientCommunicationHandler = clientCommunicationHandler;
	}

	public void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setTemplateService(final TemplateService templateService) {
		this.templateService = templateService;
	}

	/*
	 * Event handling and other functionality.
	 */

	public <M extends Message> M handleMessage(final M message) {
		M retVal = null;

		// if this is an initial request, we return the current component states
		if (MessageType.initialStateRequest.equals(message.getType())) {
			sendMessage(getViewState());

		} else if (MessageType.event.equals(message.getType())) {
			EventMessage eventMessage = (EventMessage) message;

			// handle browser history changes
			if (Events.JsEvent.PopState.equals(eventMessage.getEventType())) {
				onBrowserHistoryChange(eventMessage);
			} else if (StringUtils.isNotBlank(eventMessage.getComponentUuid())) {
				final Component component = getRegisteredComponents().get(eventMessage.getComponentUuid());

				// apply changed states to the component
				if (GenericEvent.StateChanged.equals(eventMessage.getEventType())) {
					ObjectUtils.populate(component, eventMessage.getPayload());
				}

				handleEvent(component, eventMessage.getEventType(), eventMessage.getPayload());
			} else {
				LOG.warn("Received message of unknown sender component.");
			}
		} else {
			LOG.warn(String.format("Could not handle message of type %s", message.getType()));
		}

		return retVal;
	}

	protected void handleEvent(final Component component, final EventType eventType,
			final Map<String, Object> payload) {
		try {
			((EventTarget) component).handleEvent(new Event(eventType, component, payload));
		} catch (Exception ex) {
			LOG.error(String.format("Exception during handleEvent for component %s", component.getUuid()), ex);
			throw ex;
		}

		flushClientUpdates();
	}

	protected void flushClientUpdates() {
		for (final Component c : getRegisteredComponents().values()) {
			flushClientUpdates(c);
		}
	}

	@Override
	public void flushClientUpdates(Component component) {
		if (component.hasPendingClientUpdateCommands()) {
			for (ClientUpdateCommand cmd : component.getClientUpdateCommands()) {
				switch (cmd.getType()) {
				case ComponentStateUpdate:
					updateComponentState(component);
					break;
				case FunctionCall:
					invokeFunctionCall(cmd.getTargetObject(), cmd.getFunction(), cmd.getParamters());
					break;
				case ObjectManipulation:
					invokeComponentManipulation(component, cmd.getFunction(), cmd.getParamters());
					break;
				default:
					break;
				}
			}

			// clear draw commands of the current component, as they have
			// all been worked off
			component.clearPendingClientUpdateCommands();
		}
	}

	/**
	 * Calls a javascript function an passes the given parameters.
	 * 
	 * @param method
	 * @param parameters
	 */
	public void invokeFunctionCall(final String object, final String functionCall, final Object... parameters) {
		FunctionCallMessage message = new FunctionCallMessage();

		message.setObject(object);
		message.setFunctionCall(functionCall);
		message.setParameters(parameters);

		sendMessage(message);
	}

	/**
	 * Calls a javascript function on the given object with the given
	 * parameters.
	 * 
	 * @param component
	 * @param method
	 * @param parameters
	 */
	public void invokeComponentManipulation(final Component component, final String method,
			final Object... parameters) {

		final ComponentManipulationMessage message = new ComponentManipulationMessage();

		message.setComponentUuid(component.getUuid());
		message.setMethod(method);
		message.setParameters(parameters);

		sendMessage(message);
	}

	public void updateComponentState(final Component component) {

		ComponentStateUpdateMessage message = new ComponentStateUpdateMessage();
		message.setComponent(component);

		sendMessage(message);
	}

	public void redrawComponentData(final AbstractComponent component) {
		invokeFunctionCall("jfly", "replaceComponent", component.getUuid(), component.render());
	}

	public <M extends Message> void sendMessage(final M message) {
		clientCommunicationHandler.sendMessage(message, getSessionId());
	}

	public Map<String, Component> getRegisteredComponents() {
		return registeredComponents;
	}

	@Override
	public void registerComponent(final Component component) {
		registeredComponents.put(component.getUuid(), component);
	}

	@Override
	public String renderComponent(final AbstractComponent component) {
		String output = "<could not render>";

		if (templateService != null) {
			String events = "";

			for (final EventType event : component.getRegisteredEvents()) {
				events += String.format(" v-on:%s=\"handleEvent('%s', '%s', $event)\"", event.toString(),
						event.toString(), component.getUuid());
			}

			ComponentContext context = new ComponentContext();
			context.setComponent(component);
			context.put("events", events);
			context.put("_events", events);
			context.put("_uuid", component.getUuid());
			context.put("_state", "componentStates['" + component.getUuid() + "']");

			output = templateService.render(context, getTemplateBaseName(component.getClass()) + ".vm");
		}

		return output;
	}

	protected String getTemplateBaseName(Class<? extends AbstractComponent> componentClass) {
		try {
			Template templateAnnotation = componentClass.getAnnotation(Template.class);

			if (templateAnnotation != null) {
				if (StringUtils.isNotBlank(templateAnnotation.useTemplateFilename())) {
					return templateAnnotation.useTemplateFilename();
				} else {
					return getTemplateBaseName(templateAnnotation.useTemplateOf());
				}
			}
		} catch (NullPointerException e) {
			// ignore
		}

		return componentClass.getName();
	}

	public <M extends Message> M getViewState() {
		final IntialStateUpdateMessage message = new IntialStateUpdateMessage();

		message.getComponentStates().putAll(getRegisteredComponents());

		message.getGlobalState().put("currentLocale", getCurrentLocale());
		message.getGlobalState().put("supportedLocales", getSupportedLocales());
		message.getGlobalState().put("eventDelay", 200);

		return (M) message;
	}

	protected abstract List<Locale> getSupportedLocales();

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public void destroy() {
		onDestroyEventListener.stream().forEach(e -> e.run());
		invokeFunctionCall("jfly", "reloadApp");
	}

	public void onDestroy(Runnable eventListener) {
		onDestroyEventListener.add(eventListener);
	}

	protected void onBrowserHistoryChange(EventMessage eventMessage) {
		// to be implemented by subclasses
	}

	/*
	 * HTML Rendering
	 */

	public ViewHandler getHandler() {
		return this;
	}

	protected abstract Head createHeader();

	protected abstract Body createBody();

	public Head head() {
		return html.getHead();
	}

	public Body body() {
		return html.getBody();
	}

	public String render() {
		String ret = html.render();

		if (formatOutput) {
			ret = format(ret);
		}

		return ret;
	}

	public String format(String html) {
		Document doc = Jsoup.parse(html);
		return doc.outerHtml();
	}
}