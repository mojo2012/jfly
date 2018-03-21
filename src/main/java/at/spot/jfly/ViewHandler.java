package at.spot.jfly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.html.Html;
import at.spot.jfly.util.GsonUtil;

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
	 * Defines the path under which the velocity template files for rendering the UI
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

	public Object handleMessage(final JsonObject msg, final String urlPath) {
		Object retVal = null;
		// if this is an initial request, we return the current component states
		if (msg.get("messageType") != null
				&& StringUtils.equalsIgnoreCase(msg.get("messageType").getAsString(), "init")) {

			retVal = getViewState();
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
			LOG.error(String.format("Exception during handleEvent for component %s", component.getUuid()), ex);
			throw ex;
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

	public Map<String, Object> getViewState() {
		final Map<String, Object> compState = new HashMap<>();

		compState.put("type", "componentInitialization");
		compState.put("componentStates", getRegisteredComponents());

		Map<String, Object> globalState = new HashMap<>();
		compState.put("globalState", globalState);

		Map<String, Object> currentLocale = new HashMap<>();
		globalState.put("currentLocale", currentLocale);

		currentLocale.put("language", getCurrentLocale().getLanguage());
		currentLocale.put("country", getCurrentLocale().getCountry());
		currentLocale.put("code", getCurrentLocale());

		return compState;
	}

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