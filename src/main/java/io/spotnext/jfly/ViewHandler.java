package io.spotnext.jfly;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.spotnext.jfly.event.DomEvent;
import io.spotnext.jfly.event.Events;
import io.spotnext.jfly.event.Events.EventType;
import io.spotnext.jfly.event.Events.GenericEvent;
import io.spotnext.jfly.http.websocket.ComponentManipulationMessage;
import io.spotnext.jfly.http.websocket.ComponentStateUpdateMessage;
import io.spotnext.jfly.http.websocket.EventMessage;
import io.spotnext.jfly.http.websocket.FunctionCallMessage;
import io.spotnext.jfly.http.websocket.IntialStateUpdateMessage;
import io.spotnext.jfly.http.websocket.Message;
import io.spotnext.jfly.http.websocket.Message.MessageType;
import io.spotnext.jfly.templating.ComponentContext;
import io.spotnext.jfly.templating.Template;
import io.spotnext.jfly.templating.TemplateService;
import io.spotnext.jfly.templating.impl.VelocityTemplateService;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.ui.base.ClientUpdateCommand;
import io.spotnext.jfly.ui.base.Component;
import io.spotnext.jfly.ui.base.DrawCommandType;
import io.spotnext.jfly.ui.base.EventTarget;
import io.spotnext.jfly.ui.html.Body;
import io.spotnext.jfly.ui.html.Head;
import io.spotnext.jfly.ui.html.Html;
import io.spotnext.jfly.util.JsonUtil;
import io.spotnext.jfly.util.ObjectUtils;

public abstract class ViewHandler implements ComponentHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ViewHandler.class);

	public static final String DEFAULT_COMPONENT_TEMPLATE_PATH = "/template/component";

	protected transient LocalDateTime lastKeepAlive;

	protected final Map<String, Component> registeredComponents = new ConcurrentHashMap<>();
	protected TemplateService templateService;
	protected ClientCommunicationHandler clientCommunicationHandler;

	protected String componentTemplatePath;
	protected String sessionId;

	private Html html;

	// this can destroy the HTML output! (eg. removes misplaces <tr> tags
	private boolean formatOutput = false;
	private Locale currentLocale = Locale.getDefault();

	protected final List<Consumer<ViewHandler>> onDestroyEventListener = new ArrayList<>();

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

		setupRouting(html.getBody());
	}

	private void setupRouting(Body body) {
		body.onLocationChange(this::onLocationChanged);
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
			route(message.getUrl(), false);
			sendMessage(getApplicationState());
		} else if (MessageType.event.equals(message.getType())) {
			EventMessage eventMessage = (EventMessage) message;

			String componentUuid = eventMessage.getComponentUuid();

			// handle browser history changes
			if (Events.JsEvent.PopState.equals(eventMessage.getEventType())
					|| Events.JsEvent.HashChange.equals(eventMessage.getEventType())) {
				onLocationChanged(eventMessage.getDomEventData());
				flushClientUpdates();

			} else if (Events.JsEvent.BeforeUnload.equals(eventMessage.getEventType())) {
				// before unload is also called on browser refresh
				// getHandler().destroy();

			} else if (StringUtils.isNotBlank(componentUuid)) {
				final Component component = getRegisteredComponents().get(componentUuid);

				if (component instanceof AbstractComponent) {
					// apply changed states to the component
					if (GenericEvent.StateChanged.equals(eventMessage.getEventType())) {
						ObjectUtils.populate(component, eventMessage.getDomEventData().getData());
					}

					component.clearPendingClientUpdateCommands();

					if (((AbstractComponent) component).isEventHandled(eventMessage.getEventType())) {
						handleEvent(component, eventMessage.getEventType(), eventMessage.getDomEventData());
					}
				} else {
					LOG.warn(String.format("Received message of unknown sender component %s", componentUuid));
				}
			} else {
				LOG.warn(String.format("Received message of unknown sender component %s", componentUuid));
			}
		} else {
			LOG.warn(String.format("Could not handle message of type %s", message.getType()));
		}

		return retVal;
	}

	protected void handleEvent(final Component component, final EventType eventType, final DomEvent payload) {
		try {
			((EventTarget) component).handleEvent(payload);
		} catch (Exception ex) {
			LOG.error(String.format("Exception during handleEvent for component %s", component.getUuid()), ex);
			throw ex;
		}

		flushClientUpdates();
	}

	protected void flushClientUpdates() {
		// find the components that need to be updated
		final List<Component> components = getRegisteredComponents().values().stream() //
				.filter(c -> c.hasPendingClientUpdateCommands()) //
				.collect(Collectors.toList());

		for (final Component c : components) {
			flushClientUpdates(c);
		}
	}

	@Override
	public void flushClientUpdates(Component component) {
		if (component.hasPendingClientUpdateCommands()) {
			final Set<ClientUpdateCommand> updateCommands = component.getClientUpdateCommands();

			if (updateCommands.size() > 0) {
				final Optional<ClientUpdateCommand> fullComponentUpdate = updateCommands.stream() //
						.filter(c -> DrawCommandType.ComponentStateUpdate.equals(c.getType())) //
						.max((c1, c2) -> c1.getCreationTimestamp().compareTo(c2.getCreationTimestamp()));

				for (ClientUpdateCommand cmd : component.getClientUpdateCommands()) {
					switch (cmd.getType()) {
					case ComponentStateUpdate:

						// only execute the last created component update,
						// ignore
						// the rest, to save some bandwidth and client updates
						if (fullComponentUpdate.isEmpty() || cmd.equals(fullComponentUpdate.get())) {
							updateComponentState(component, null);
						}
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

	public void updateComponentState(final Component component, Set<String> propertyBeanPaths) {

		ComponentStateUpdateMessage message = new ComponentStateUpdateMessage();

		if (propertyBeanPaths == null) {
			message.setComponent(component);
		}

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
		return renderComponent(component, null);
	}

	@Override
	public String renderComponent(final AbstractComponent component, Map<String, Object> additionalValues) {
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

			if (additionalValues != null) {
				context.putAll(additionalValues);
			}

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

	public <M extends Message> M getApplicationState() {
		final IntialStateUpdateMessage message = new IntialStateUpdateMessage();

		message.getComponentStates().putAll(getRegisteredComponents());

		message.getGlobalState().put("currentLocale", getCurrentLocale());
		message.getGlobalState().put("supportedLocales", getSupportedLocales());
		message.getGlobalState().put("eventDelay", 200);

		return (M) message;
	}

	@Override
	public void showErrorMessage(String message) {
		clientCommunicationHandler.sendErrorMessage(message, null);
	}

	protected abstract List<Locale> getSupportedLocales();

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public void destroy() {
		onDestroyEventListener.stream().forEach(e -> e.accept(this));
		invokeFunctionCall("jfly", "reloadApp");
	}

	public void onDestroy(Consumer<ViewHandler> eventListener) {
		onDestroyEventListener.add(eventListener);
	}

	public void onLocationChanged(DomEvent event) {
		String url = (String) event.getData().get("currentUrl");
		String urlHash = (String) event.getData().get("currentUrlHash");

		route(url + urlHash, true);
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

		ret = StringUtils.replace(ret, "${componentStates}", JsonUtil.toJson(getApplicationState()));
		ret = StringUtils.replace(ret, "${viewId}", JsonUtil.toJson(this.getViewUid()));

		if (formatOutput) {
			ret = format(ret);
		}

		return ret;
	}

	public String format(String html) {
		final Document doc = Jsoup.parse(html);
		return doc.outerHtml();
	}

	public abstract String getViewUid();

	public abstract void route(String url, boolean flushChanges);
}