package at.spot.jfly.ui.selection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.ButtonStyle;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.ui.action.Button;
import io.gsonfire.annotations.ExposeMethodResult;

public class SingleButtonDropDown extends Button {

	final private transient Map<String, SelectMenuItem> menuItems = new HashMap<>();

	public SingleButtonDropDown(final ComponentHandler handler, final String text) {
		super(handler, text);
		componentType(ComponentType.Button);
		addStyleClasses(ButtonStyle.None);
		super.eventData("'value'");
		super.eventData("$event");

		this.onEvent(JsEvent.input, (e) -> {
			// forward the select event to the appropriate menu item event handler

			Object itemId = e.getPayload().get("value");
			SelectMenuItem item = menuItems.get(itemId);

			if (item != null && item.handler != null)
				item.handler.handle(e);
		});
	}

	public SingleButtonDropDown addMenuItem(final String itemId, String text, final EventHandler handler) {
		menuItems.put(itemId, new SelectMenuItem(itemId, text, null, handler));

		return this;
	}

	public SingleButtonDropDown removeMenuItem(final String itemId) {
		SelectMenuItem item = menuItems.get(itemId);

		if (item != null && item.handler != null) {
			unregisterEventHandler(JsEvent.select, item.handler);
		}

		return this;

	}

	@ExposeMethodResult("menuItems")
	public Collection<SelectMenuItem> menuItems() {
		return menuItems.values();
	}

	private static class SelectMenuItem {
		private String id;
		private String text;
		private String icon;
		private transient EventHandler handler;

		public SelectMenuItem(String id, String text, String icon, EventHandler handler) {
			this.id = id;
			this.text = text;
			this.icon = icon;
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
