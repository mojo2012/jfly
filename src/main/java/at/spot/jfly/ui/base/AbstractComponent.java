package at.spot.jfly.ui.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.Event;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.style.Style;
import at.spot.jfly.util.KeyValueListMapping;
import at.spot.jfly.util.KeyValueMapping;
import io.gsonfire.annotations.ExposeMethodResult;

public abstract class AbstractComponent implements Component, EventTarget, Comparable<AbstractComponent> {

	@JsonIgnore
	private final transient List<DrawCommand> drawCommands = new LinkedList<>();
	@JsonIgnore
	private final transient List<String> eventData = new ArrayList<>();
	@JsonIgnore
	private transient ComponentHandler handler;
	@JsonIgnore
	private transient ComponentType componentType;

	private String type;
	private final String uuid;
	private final Set<String> styleClasses = new HashSet<>();
	private final KeyValueMapping<String, String> attributes = new KeyValueMapping<>();
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

	public <C extends AbstractComponent> C componentType(final ComponentType componentType) {
		this.componentType = componentType;
		updateClientComponent();
		return (C) this;
	}

	public ComponentType getComponentType() {
		return this.componentType;
	}

	public <C extends AbstractComponent> C setVisibe(final boolean visible) {
		this.visible = visible;
		updateClientComponent();
		return (C) this;
	}

	public <C extends AbstractComponent> C addStyleClasses(final String... styleClasses) {
		final List<String> styles = Arrays.stream(styleClasses).filter(s -> StringUtils.isNotBlank(s))
				.collect(Collectors.toList());

		this.styleClasses.addAll(styles);
		updateClientComponent();
		return (C) this;
	}

	public <C extends AbstractComponent> C removeStyleClasses(final String... styleClasses) {
		final List<String> styles = Arrays.stream(styleClasses).filter(s -> StringUtils.isNotBlank(s))
				.collect(Collectors.toList());

		this.styleClasses.removeAll(styles);
		updateClientComponent();
		return (C) this;
	}

	public <C extends AbstractComponent> C addStyleClasses(final Style... styles) {
		final List<String> stylesClasses = Arrays.stream(styles).filter((s) -> s != null).map(s -> s.internalName())
				.collect(Collectors.toList());
		addStyleClasses(stylesClasses.toArray(new String[0]));
		return (C) this;
	}

	public <C extends AbstractComponent> C removeStyleClasses(final Style... styles) {
		final List<String> stylesClasses = Arrays.stream(styles).filter((s) -> s != null).map(s -> s.internalName())
				.collect(Collectors.toList());

		removeStyleClasses(stylesClasses.toArray(new String[0]));
		updateClientComponent();
		return (C) this;
	}

	public <C extends AbstractComponent> C addAttribute(final String name, String value) {
		this.attributes.put(name, value);
		updateClientComponent();
		return (C) this;
	}

	public <C extends AbstractComponent> C removeAttributes(final String... attributes) {
		this.attributes.removeAll(attributes);
		updateClientComponent();
		return (C) this;
	}

	public String getAttributeString() {
		return attributes.entrySet().stream() //
				.map(e -> e.getKey() + (e.getValue() != null ? "=" + e.getValue() : "")) //
				.collect(Collectors.joining(" "));
	}

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
	protected <C extends AbstractComponent> C onEvent(final JsEvent eventType, final EventHandler handler) {
		if (handler != null) {
			this.eventHandlers.putOrAdd(eventType, handler);
		}

		return (C) this;
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
	public boolean needsRedraw() {
		return drawCommands.size() > 0;
	}

	@Override
	public void clearDrawCommands() {
		drawCommands.clear();
	}

	@Override
	public List<DrawCommand> getDrawCommands() {
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

	// @ExposeMethodResult("styleClasses")
	// public String getCssStyleString() {
	// String classes = StringUtils.join(styleClasses, " ");
	//
	// if (!isVisible()) {
	// classes += " hidden";
	// }
	//
	// return classes;
	// }

	@ExposeMethodResult("componentType")
	protected String getComponentTypeName() {
		return componentType != null ? componentType.internalName() : null;
	}

	@Override
	public String render() {
		clearDrawCommands();
		return getHandler().renderComponent(this);
	}

	@Override
	public <C extends AbstractComponent> C redraw() {
		updateClientComponent("replace", render());
		return (C) this;
	}

	protected void updateClientComponent() {
		drawCommands.add(new DrawCommand(DrawCommandType.ComponentStateUpdate, null, null, null));
	}

	protected void updateClientComponent(final String method, final Object... params) {
		drawCommands.add(new DrawCommand(DrawCommandType.ObjectManipulation, null, method, params));
	}

	protected void callClientFunction(final String object, final String function, final Object... params) {
		drawCommands.add(new DrawCommand(DrawCommandType.FunctionCall, object, function, params));
	}

	protected void updateClient(ClientDrawFunction function, final Object... params) {
		String functionName = null;

		if (ClientDrawFunction.ADD.equals(function)) {
			functionName = "addChildComponent";
		} else if (ClientDrawFunction.REMOVE.equals(function)) {
			functionName = "removeChildComponent";
		} else if (ClientDrawFunction.REPLACE.equals(function)) {
			functionName = "replaceChildComponent";
		}

		drawCommands.add(new DrawCommand(DrawCommandType.FunctionCall, "jfly", functionName, params));
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

	public <C extends AbstractComponent> C onClick(final EventHandler handler) {
		onEvent(JsEvent.click, handler);

		return (C) this;
	}

	public <C extends AbstractComponent> C onMouseOut(final EventHandler handler) {
		onEvent(JsEvent.mouseout, handler);

		return (C) this;
	}

	public <C extends AbstractComponent> C onMouseMove(final EventHandler handler) {
		onEvent(JsEvent.mousemove, handler);

		return (C) this;
	}

	public <C extends AbstractComponent> C onMouseDown(final EventHandler handler) {
		onEvent(JsEvent.mousedown, handler);

		return (C) this;
	}

	public <C extends AbstractComponent> C onMouseOver(final EventHandler handler) {
		onEvent(JsEvent.mouseover, handler);

		return (C) this;
	}

	public <C extends AbstractComponent> C onMouseUp(final EventHandler handler) {
		onEvent(JsEvent.mouseup, handler);

		return (C) this;
	}

	public <C extends AbstractComponent> C onMouseWheel(final EventHandler handler) {
		onEvent(JsEvent.mousewheel, handler);

		return (C) this;
	}

	public enum ClientDrawFunction {
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
