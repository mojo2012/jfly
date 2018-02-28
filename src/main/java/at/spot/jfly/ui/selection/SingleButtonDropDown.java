package at.spot.jfly.ui.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.ButtonStyle;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.ui.action.Button;

public class SingleButtonDropDown extends Button {

	final private List<SelectMenuItem> menuItems = new ArrayList<>();

	public SingleButtonDropDown(final ComponentHandler handler, final String text) {
		super(handler, text);
		componentType(ComponentType.Button);
		addStyleClasses(ButtonStyle.None);
		super.eventData("'value'");
		super.eventData("$event");
	}

	public SingleButtonDropDown addMenuItem(final String itemId, String text, final EventHandler handler) {
		menuItems.add(new SelectMenuItem(itemId, text, handler));

		this.onEvent(JsEvent.input, (e) -> {
			// forward the select event to the appropriate menu item event handler
			Stream<SelectMenuItem> items = menuItems.stream().filter(m -> m.text.equals(e.getPayload().get("value")));
			items.forEach(m -> handler.handle(e));
		});
		return this;
	}

	public SingleButtonDropDown removeMenuItem(final String itemId) {
		Optional<SelectMenuItem> menuItem = menuItems.stream().filter(m -> itemId.equals(m.id)).findFirst();

		if (menuItem.isPresent() && menuItem.get().handler != null) {
			unregisterEventHandler(JsEvent.select, menuItem.get().handler);
		}

		return this;

	}

	public List<SelectMenuItem> menuItems() {
		return Collections.unmodifiableList(menuItems);
	}

	private static class SelectMenuItem {
		private String id;
		private String text;
		private transient EventHandler handler;

		public SelectMenuItem(String id, String text, EventHandler handler) {
			this.id = id;
			this.text = text;
			this.handler = handler;
		}

		public String getId() {
			return id;
		}

		public String getText() {
			return text;
		}
	}
}
