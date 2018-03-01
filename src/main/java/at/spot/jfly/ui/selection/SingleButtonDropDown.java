package at.spot.jfly.ui.selection;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.GlyphIcon;
import at.spot.jfly.ui.base.AbstractTextComponent;
import io.gsonfire.annotations.ExposeMethodResult;

public class SingleButtonDropDown extends AbstractTextComponent {
	final private transient Map<String, SelectMenuItem> menuItems = new TreeMap<>();

	private GlyphIcon leftIcon;
	private GlyphIcon rightIcon;

	public SingleButtonDropDown(final ComponentHandler handler, final String text) {
		super(handler, text);

		// set up event handling
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
		menuItems.put(itemId, new SelectMenuItem(itemId, text, false, null, handler));

		return this;
	}

	public SingleButtonDropDown removeMenuItem(final String itemId) {
		SelectMenuItem item = menuItems.get(itemId);

		if (item != null && item.handler != null) {
			unregisterEventHandler(JsEvent.select, item.handler);
		}

		return this;

	}

	public GlyphIcon leftIcon() {
		return leftIcon;
	}

	public <C extends AbstractTextComponent> C leftIcon(GlyphIcon leftIcon) {
		this.leftIcon = leftIcon;
		return (C) this;
	}

	public GlyphIcon rightIcon() {
		return rightIcon;
	}

	public <C extends AbstractTextComponent> C rightIcon(GlyphIcon rightIcon) {
		this.rightIcon = rightIcon;
		return (C) this;
	}

	@ExposeMethodResult("menuItems")
	public Collection<SelectMenuItem> menuItems() {
		return menuItems.values();
	}

	private static class SelectMenuItem {
		private String id;
		private String text;
		private String icon;
		private boolean disabled;
		private transient EventHandler handler;

		public SelectMenuItem(String id, String text, boolean disabled, String icon, EventHandler handler) {
			this.id = id;
			this.text = text;
			this.icon = icon;
			this.disabled = disabled;
			this.handler = handler;
		}
	}
}
