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

public class DropDownBox extends AbstractTextComponent {
	final private transient Map<String, SelectMenuItem> menuItems = new TreeMap<>();

	private GlyphIcon leftIcon;
	private GlyphIcon rightIcon;
	private boolean editable = false;
	private SelectMenuItem selectedItem;

	public DropDownBox(final ComponentHandler handler, final String text) {
		super(handler, text);

		// set up event handling
		super.eventData("'value'");
		super.eventData("$event");

		this.onEvent(JsEvent.input, (e) -> {
			// forward the select event to the appropriate menu item event handler

			Object itemId = e.getPayload().get("value");
			selectedItem = menuItems.get(itemId);

			if (selectedItem != null && selectedItem.handler != null)
				selectedItem.handler.handle(e);
		});
	}

	public DropDownBox addMenuItem(final String itemId, String text, final EventHandler handler) {
		SelectMenuItem item = menuItems.get(itemId);

		// remove previously added item with the same id -> unregisters event handler
		if (item != null) {
			removeMenuItem(itemId);
		}

		menuItems.put(itemId, new SelectMenuItem(itemId, text, false, null, handler));
		updateClientComponent();

		return this;
	}

	public String selectedItemId() {
		return selectedItem != null ? selectedItem.id : null;
	}

	public DropDownBox selectedItem(String itemId) {
		selectedItem = menuItems.get(itemId);
		updateClientComponent();
		return this;
	}

	public DropDownBox removeMenuItem(final String itemId) {
		menuItems.get(itemId);

		return this;
	}

	public boolean editable() {
		return editable;
	}

	public DropDownBox editable(boolean editable) {
		this.editable = editable;
		return this;
	}

	public GlyphIcon leftIcon() {
		return leftIcon;
	}

	public DropDownBox leftIcon(GlyphIcon leftIcon) {
		this.leftIcon = leftIcon;
		return this;
	}

	public GlyphIcon rightIcon() {
		return rightIcon;
	}

	public DropDownBox rightIcon(GlyphIcon rightIcon) {
		this.rightIcon = rightIcon;
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