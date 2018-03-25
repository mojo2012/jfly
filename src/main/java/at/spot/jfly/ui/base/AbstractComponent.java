package at.spot.jfly.ui.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Attributes.Attribute;
import at.spot.jfly.attributes.Styles.Style;
import at.spot.jfly.event.Event;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.util.KeyValueListMapping;
import at.spot.jfly.util.KeyValueMapping;

public abstract class AbstractComponent implements Component, EventTarget, Comparable<AbstractComponent> {

	@JsonIgnore
	private final transient List<ClientUpdateCommand> drawCommands = new LinkedList<>();
	@JsonIgnore
	private final transient List<String> eventData = new ArrayList<>();
	@JsonIgnore
	private transient ComponentHandler handler;

	private String type;
	private final String uuid;
	private final Set<String> styleClasses = new HashSet<>();
	private final KeyValueMapping<Attribute, String> attributes = new KeyValueMapping<>();
	private boolean visible = true;

	/*
	 * Event handlers
	 */
	protected transient KeyValueListMapping<JsEvent, EventHandler> eventHandlers = new KeyValueListMapping<>();

	/*
	 * Initialization
	 */

	public AbstractComponent() {
		// this strange uid is necessary, as we are also using this for vue
		// property binding
		// there are no dashes allowed, and each uuid must start with a letter.
		uuid = "comp" + Math.abs(UUID.randomUUID().toString().hashCode());
		type = this.getClass().getSimpleName();
	}

	protected AbstractComponent(final ComponentHandler handler) {
		this();
		this.handler = handler;
		getHandler().registerComponent(this);
	}

	/*
	 * Properties
	 */

	public Set<JsEvent> registeredEvents() {
		return this.eventHandlers.keySet();
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisibe(final boolean visible) {
		this.visible = visible;
		updateClientComponent();
	}

	public Map<Attribute, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	public Set<String> getStyleClasses() {
		return Collections.unmodifiableSet(styleClasses);
	}

	public void addStyleClass(final String styleClass) {
		this.styleClasses.add(styleClass);
		updateClientComponent();
	}

	public void addStyleClass(final Style styleClass) {
		addStyleClass(styleClass.getInternalName());
	}

	public void removeStyleClass(final String styleClass) {
		this.styleClasses.remove(styleClass);
		updateClientComponent();
	}

	public void removeStyleClass(final Style styleClass) {
		removeStyleClass(styleClass.getInternalName());
	}

	public void addAttribute(final Attribute attribute) {
		addAttribute(attribute, attribute.getInternalName());
	}

	public void addAttribute(final Attribute attribute, String value) {
		this.attributes.put(attribute, value);
		updateClientComponent();
	}

	public void removeAttribute(final Attribute attribute) {
		this.attributes.remove(attribute);
		updateClientComponent();
	}

	// public String getAttributeString() {
	// return attributes.entrySet().stream() //
	// .map(e -> e.getKey() + (e.getValue() != null ? "=" + e.getValue() : "")) //
	// .collect(Collectors.joining(" "));
	// }

	/**
	 * Returns an immutable set of the style classes.
	 */
	public Set<String> styleClasses() {
		return Collections.unmodifiableSet(styleClasses);
	}

	/*
	 * Events
	 */

	/**
	 * Registers an event handler for the given javascript event. The event can be
	 * unregistered by passing null as handler.
	 * 
	 * @param eventType
	 * @param handler
	 * @return
	 */
	protected void onEvent(final JsEvent eventType, final EventHandler handler) {
		if (handler != null) {
			this.eventHandlers.putOrAdd(eventType, handler);
		}
	}

	public void unregisterEventHandler(JsEvent eventType, EventHandler handler) {
		eventHandlers.remove(eventType, handler);
	}

	@Override
	public void handleEvent(final Event event) {
		final List<EventHandler> handlers = eventHandlers.get(event.getEventType());

		handlers.stream().forEach((h) -> h.handle(event));
	}

	@Override
	public boolean hasPendingClientUpdateCommands() {
		return drawCommands.size() > 0;
	}

	@Override
	public void clearPendingClientUpdateCommands() {
		drawCommands.clear();
	}

	@JsonIgnore
	@Override
	public List<ClientUpdateCommand> getClientUpdateCommands() {
		return drawCommands;
	}

	public void setEventData(String data) {
		this.eventData.add(data);
	}

	public List<String> getEventData() {
		return this.eventData;
	}

	/*
	 * INTERNAL
	 */

	protected ComponentHandler getHandler() {
		return handler;
	}

	public String getRegisteredEventsString() {
		return StringUtils.join(registeredEvents(), " ");
	}

	@Override
	public String render() {
		clearPendingClientUpdateCommands();
		return getHandler().renderComponent(this);
	}

	@Override
	public void redraw() {
		updateClientComponent("replace", render());
	}

	protected void updateClientComponent() {
		drawCommands.add(new ClientUpdateCommand(DrawCommandType.ComponentStateUpdate, null, null, null));
	}

	protected void updateClientComponent(final String method, final Object... params) {
		drawCommands.add(new ClientUpdateCommand(DrawCommandType.ObjectManipulation, null, method, params));
	}

	protected void callClientFunction(final String object, final String function, final Object... params) {
		drawCommands.add(new ClientUpdateCommand(DrawCommandType.FunctionCall, object, function, params));
	}

	protected void updateClient(ComponentManipulationFunction function, final Object... params) {
		String functionName = null;

		if (ComponentManipulationFunction.ADD.equals(function)) {
			functionName = "addChildComponent";
		} else if (ComponentManipulationFunction.REMOVE.equals(function)) {
			functionName = "removeChildComponent";
		} else if (ComponentManipulationFunction.REPLACE.equals(function)) {
			functionName = "replaceChildComponent";
		}

		drawCommands.add(new ClientUpdateCommand(DrawCommandType.FunctionCall, "jfly", functionName, params));
	}

	@Override
	public int compareTo(final AbstractComponent o) {
		if (o == null) {
			return 1;
		}

		return getUuid().compareTo(o.getUuid());
	}

	/*
	 * EVENT HANDLERS
	 */

	public void onClick(final EventHandler handler) {
		onEvent(JsEvent.click, handler);
	}

	public void onMouseOut(final EventHandler handler) {
		onEvent(JsEvent.mouseout, handler);
	}

	public void onMouseMove(final EventHandler handler) {
		onEvent(JsEvent.mousemove, handler);
	}

	public void onMouseDown(final EventHandler handler) {
		onEvent(JsEvent.mousedown, handler);
	}

	public void onMouseOver(final EventHandler handler) {
		onEvent(JsEvent.mouseover, handler);
	}

	public void onMouseUp(final EventHandler handler) {
		onEvent(JsEvent.mouseup, handler);
	}

	public void onMouseWheel(final EventHandler handler) {
		onEvent(JsEvent.mousewheel, handler);
	}

	public enum ComponentManipulationFunction {
		/**
		 * Adds an element as a new child element to the parent.
		 */
		ADD,
		/**
		 * Appends an element after the parent element.
		 */
		APPEND,
		/**
		 * Removes an element.
		 */
		REMOVE,
		/**
		 * Replaces an element with another one.
		 */
		REPLACE
	}
}
