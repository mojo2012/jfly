package at.spot.jfly;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;

import at.spot.jfly.event.Event;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.templating.TemplateService;
import at.spot.jfly.templating.impl.VelocityTemplateService;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.Component;
import at.spot.jfly.ui.base.DrawCommand;
import at.spot.jfly.util.GsonUtil;

public abstract class Application implements ComponentHandler {
	public static final String DEFAULT_COMPONENT_TEMPLATE_PATH = "/template/component";

	protected final Map<String, Component> registeredComponents = new ConcurrentHashMap<>();
	protected TemplateService templateService;
	protected ClientCommunicationHandler clientCommunicationHandler;

	protected String componentTemplatePath;
	protected String sessionId;

	protected final List<Runnable> onDestroyEventListener = new ArrayList<>();;

	public <A extends Application> A init(final ClientCommunicationHandler handler, final String sessionId) {
		sessionId(sessionId);
		clientCommunicationHandler(handler);

		if (StringUtils.isBlank(componentTemplatePath)) {
			this.componentTemplatePath = DEFAULT_COMPONENT_TEMPLATE_PATH;
		}

		if (this.templateService == null) {
			this.templateService = new VelocityTemplateService(componentTemplatePath);
		}

		if (clientCommunicationHandler == null) {
			throw new RuntimeException("No client communication handler set");
		}

		return (A) this;
	}

	/**
	 * Defines the path under which the velocity template files for rendering the ui
	 * components in the browser is looked for. The path has to be in the
	 * classpath.<br />
	 * Default: {@link Server#DEFAULT_COMPONENT_TEMPLATE_PATH}
	 */
	public <A extends Application> A componentTemplatePath(final String path) {
		this.componentTemplatePath = path;
		return (A) this;
	}

	public <A extends Application> A clientCommunicationHandler(
			final ClientCommunicationHandler clientCommunicationHandler) {
		this.clientCommunicationHandler = clientCommunicationHandler;
		return (A) this;
	}

	public <A extends Application> A sessionId(final String sessionId) {
		this.sessionId = sessionId;
		return (A) this;
	}

	public String sessionId() {
		return this.sessionId;
	}

	public void templateService(final TemplateService templateService) {
		this.templateService = templateService;
	}

	/*
	 * Event handling and other functionality.
	 */

	/**
	 * Renders the corresponding {@link Window} for the given url path.
	 * 
	 * @param urlPath
	 * @return
	 */
	public abstract String render(final String urlPath);

	public Object handleMessage(final JsonObject msg, final String urlPath) {
		Object retVal = null;
		// if this is an initial request, we return the current component states
		if (msg.get("messageType") != null
				&& StringUtils.equalsIgnoreCase(msg.get("messageType").getAsString(), "hello")) {

			retVal = getInitialComponentStates();
		} else { // this is a regular message, most likely an event
			final String componentUuid = msg.get("uuid").getAsString();

			if (StringUtils.isNotBlank(componentUuid)) {
				final Component component = getRegisteredComponents().get(componentUuid);
				final String event = msg.get("event").getAsString();
				final Map<String, Object> payload = GsonUtil.fromJson(msg.get("payload"), Map.class);

				handleEvent(component, event, payload);
			}
		}

		return retVal;
	}

	public Map<String, Component> getRegisteredComponents() {
		return registeredComponents;
	}

	@Override
	public void registerComponent(final Component component) {
		registeredComponents.put(component.uuid(), component);
	}

	protected void handleEvent(final Component component, final String event, final Map<String, Object> payload) {
		final JsEvent e = JsEvent.valueOf(event);
		component.handleEvent(new Event(e, component, payload));

		for (final Component c : getRegisteredComponents().values()) {
			if (c.needsRedraw()) {
				for (DrawCommand cmd : c.getDrawCommands()) {
					switch (cmd.getType()) {
					case ComponentStateUpdate:
						updateComponentData(component);
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
				c.clearDrawCommands();
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
		final Map<String, Object> message = new HashMap<>();

		message.put("type", "functionCall");
		message.put("object", object);
		message.put("func", functionCall);
		message.put("params", parameters);

		sendMessage(message);
	}

	/**
	 * Calls a javascript function on the given object with the given parameters.
	 * 
	 * @param component
	 * @param method
	 * @param parameters
	 */
	public void invokeComponentManipulation(final Component component, final String method,
			final Object... parameters) {

		final Map<String, Object> message = new HashMap<>();

		message.put("type", "objectManipulation");
		message.put("componentUuid", component.uuid());
		message.put("method", method);
		message.put("params", parameters);

		sendMessage(message);
	}

	public void updateComponentData(final Component component) {
		final String data = component.toJson();

		final Map<String, Object> message = new HashMap<>();

		message.put("type", "componentUpdate");
		message.put("componentUuid", component.uuid());
		message.put("componentState", component);

		sendMessage(message);
	}

	public void redrawComponentData(final AbstractComponent component) {
		invokeFunctionCall("jfly", "replaceComponent", component.uuid(), component.render());
	}

	public void sendMessage(final Map<String, Object> message) {
		clientCommunicationHandler.sendMessage(message);
	}

	@Override
	public String renderComponent(final AbstractComponent component) {
		String output = "<could not render>";

		if (templateService != null) {
			final InputStream stream = this.getClass().getClassLoader()
					.getSystemResourceAsStream(component.getClass().getSimpleName());

			String events = "";

			for (final JsEvent event : component.registeredEvents()) {
				events += " v-on:" + event.toString() + "=\"handleEvent\" ";
			}

			final Map<String, Object> context = new HashMap<>();
			context.put("component", component);
			context.put("events", events);

			output = templateService.render(context, component.getClass().getName() + ".vm");
		}

		return output;
	}

	public Map<String, Object> getInitialComponentStates() {
		final Map<String, Object> compState = new HashMap<>();

		compState.put("type", "componentInitialization");
		compState.put("componentStates", getRegisteredComponents());

		return compState;
	}

	public void destroy() {
		onDestroyEventListener.stream().forEach(e -> e.run());
		invokeFunctionCall("jfly", "reloadApp");
	}

	public void onDestroy(Runnable eventListener) {
		onDestroyEventListener.add(eventListener);
	}
}