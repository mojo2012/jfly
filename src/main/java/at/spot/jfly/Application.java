package at.spot.jfly;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import at.spot.jfly.event.Event;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.templating.ComponentContext;
import at.spot.jfly.templating.TemplateService;
import at.spot.jfly.templating.impl.VelocityTemplateService;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.Component;
import at.spot.jfly.ui.base.DrawCommand;
import at.spot.jfly.ui.base.EventTarget;
import at.spot.jfly.util.GsonUtil;

public abstract class Application implements ComponentHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	public static final String DEFAULT_COMPONENT_TEMPLATE_PATH = "/template/component";

	protected final Map<String, Component> registeredComponents = new ConcurrentHashMap<>();
	protected TemplateService templateService;
	protected ClientCommunicationHandler clientCommunicationHandler;

	protected String componentTemplatePath;
	protected String sessionId;

	protected final List<Runnable> onDestroyEventListener = new ArrayList<>();;

	public void init(final ClientCommunicationHandler handler, final String sessionId) {
		setSessionId(sessionId);
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

	}

	/**
	 * Defines the path under which the velocity template files for rendering the ui
	 * components in the browser is looked for. The path has to be in the
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
				&& StringUtils.equalsIgnoreCase(msg.get("messageType").getAsString(), "init")) {

			retVal = getInitialComponentStates();
		} else { // this is a regular message, most likely an event
			JsonElement uuidElem = msg.get("componentUuid");

			if (uuidElem != null) {
				final String componentUuid = uuidElem.getAsString();

				if (StringUtils.isNotBlank(componentUuid)) {
					final Component component = getRegisteredComponents().get(componentUuid);
					final String event = msg.get("event").getAsString();
					final Map<String, Object> payload = GsonUtil.fromJson(msg.get("payload"), Map.class);

					handleEvent(component, event, payload);
				}
			} else {
				LOG.warn("Received message of unknown sender component.");
			}
		}

		return retVal;
	}

	public Map<String, Component> getRegisteredComponents() {
		return registeredComponents;
	}

	@Override
	public void registerComponent(final Component component) {
		registeredComponents.put(component.getUuid(), component);
	}

	protected void handleEvent(final Component component, final String event, final Map<String, Object> payload) {
		final JsEvent e = JsEvent.valueOf(event);

		try {
			((EventTarget) component).handleEvent(new Event(e, component, payload));
		} catch (Exception ex) {
			LOG.debug(String.format("Exception during handleEvent for component %s", component.getUuid()));
			ex.printStackTrace();
		}

		for (final Component c : getRegisteredComponents().values()) {
			if (c.needsRedraw()) {
				for (DrawCommand cmd : c.getDrawCommands()) {
					switch (cmd.getType()) {
					case ComponentStateUpdate:
						updateComponentData(c);
						break;
					case FunctionCall:
						invokeFunctionCall(cmd.getTargetObject(), cmd.getFunction(), cmd.getParamters());
						break;
					case ObjectManipulation:
						invokeComponentManipulation(c, cmd.getFunction(), cmd.getParamters());
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
		message.put("componentUuid", component.getUuid());
		message.put("method", method);
		message.put("params", parameters);

		sendMessage(message);
	}

	public void updateComponentData(final Component component) {
		final String data = component.toJson();

		final Map<String, Object> message = new HashMap<>();

		message.put("type", "componentUpdate");
		message.put("componentUuid", component.getUuid());
		message.put("componentState", component);

		sendMessage(message);
	}

	public void redrawComponentData(final AbstractComponent component) {
		invokeFunctionCall("jfly", "replaceComponent", component.getUuid(), component.render());
	}

	public void sendMessage(final Map<String, Object> message) {
		clientCommunicationHandler.sendMessage(message);
	}

	@Override
	public String renderComponent(final AbstractComponent component) {
		String output = "<could not render>";

		if (templateService != null) {
			String events = "";

			for (final JsEvent event : component.registeredEvents()) {
				events += String.format(" v-on:%s=\"handleEvent('%s', '%s', $event)\"", event.toString(),
						event.toString(), component.getUuid());
			}

			ComponentContext context = new ComponentContext();
			context.setComponent(component);
			context.put("events", events);
			context.put("_events", events);
			context.put("_uuid", component.getUuid());
			context.put("_state", "componentStates['" + component.getUuid() + "']");

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