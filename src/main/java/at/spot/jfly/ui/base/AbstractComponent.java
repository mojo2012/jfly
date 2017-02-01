package at.spot.jfly.ui.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.Event;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.style.Style;
import io.gsonfire.annotations.ExposeMethodResult;

public abstract class AbstractComponent implements Component, Comparable<AbstractComponent> {

	private final String uuid;
	private final transient List<DrawCommand> drawCommands = new LinkedList<>();
	private final transient Set<String> styleClasses = new HashSet<>();

	private transient ComponentHandler handler;
	private transient ComponentType componentType;
	private boolean visible = true;

	/*
	 * Event handlers
	 */
	protected transient Map<JsEvent, EventHandler> eventHandlers = new HashMap<>();

	/*
	 * Initialization
	 */

	protected AbstractComponent(final ComponentHandler handler) {
		// this strange uid is necessary, as we are also using this for vue
		// property binding
		// there are no dashes allowed, and each uuid must start with a letter.
		uuid = "comp" + Math.abs(UUID.randomUUID().toString().hashCode());
		this.handler = handler;
		handler().registerComponent(this);
	}

	/*
	 * Properties
	 */

	public Set<JsEvent> registeredEvents() {
		return this.eventHandlers.keySet();
	}

	@Override
	public String uuid() {
		return uuid;
	}

	public boolean visibility() {
		return visible;
	}

	public <C extends AbstractComponent> C componentType(final ComponentType componentType) {
		this.componentType = componentType;
		updateClientComponent();
		return (C) this;
	}

	public ComponentType componentType() {
		return this.componentType;
	}

	public <C extends AbstractComponent> C visibe(final boolean visible) {
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

	/**
	 * Returns an immutable set of the style classes.
	 */
	public Set<String> styleClasses() {
		return Collections.unmodifiableSet(styleClasses);
	}

	/*
	 * Events
	 */

	@Override
	public <C extends AbstractComponent> C onEvent(final JsEvent eventType, final EventHandler handler) {
		if (handler != null) {
			this.eventHandlers.put(eventType, handler);
		} else {
			this.eventHandlers.remove(eventType);
		}

		return (C) this;
	}

	@Override
	public void handleEvent(final Event event) {
		final EventHandler eventHandler = eventHandlers.get(event.getEventType());

		eventHandler.handle(event);
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

	/*
	 * INTERNAL
	 */

	protected ComponentHandler handler() {
		return handler;
	}

	public String getRegisteredEventsString() {
		return StringUtils.join(registeredEvents(), " ");
	}

	@ExposeMethodResult("styleClasses")
	public String getCssStyleString() {
		String classes = StringUtils.join(styleClasses, " ");

		if (!visibility()) {
			classes += " hidden";
		}

		return classes;
	}

	@ExposeMethodResult("componentType")
	protected String getComponentType() {
		return componentType != null ? componentType.internalName() : null;
	}

	@Override
	public String render() {
		clearDrawCommands();
		return handler().renderComponent(this);
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

	protected void updateClient(final String object, final String function, final Object... params) {
		drawCommands.add(new DrawCommand(DrawCommandType.FunctionCall, object, function, params));
	}

	@Override
	public int compareTo(final AbstractComponent o) {
		if (o == null) {
			return 1;
		}

		return uuid().compareTo(o.uuid());
	}
}
