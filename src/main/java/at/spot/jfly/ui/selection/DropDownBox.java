package at.spot.jfly.ui.selection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.style.MaterialIcon;
import at.spot.jfly.ui.base.AbstractTextComponent;
import at.spot.jfly.util.Localizable;
import io.gsonfire.annotations.ExposeMethodResult;

public class DropDownBox extends AbstractTextComponent {
	final private transient Map<String, SelectMenuItem> menuItems = new TreeMap<>();

	private MaterialIcon leftIcon;
	private MaterialIcon rightIcon;
	private boolean editable = false;
	private SelectMenuItem selectedItem;

	public DropDownBox(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);

		// set up event handling
		super.setEventData("'value'");
		super.setEventData("$event");

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

	public String getSelectedItemId() {
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

	public boolean isEditable() {
		return editable;
	}

	public DropDownBox setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}

	public MaterialIcon getLeftIcon() {
		return leftIcon;
	}

	public DropDownBox setLeftIcon(MaterialIcon leftIcon) {
		this.leftIcon = leftIcon;
		return this;
	}

	public MaterialIcon getRightIcon() {
		return rightIcon;
	}

	public void setRightIcon(MaterialIcon rightIcon) {
		this.rightIcon = rightIcon;
	}

	@ExposeMethodResult("menuItems")
	public Collection<SelectMenuItem> getMenuItems() {
		return menuItems.values();
	}

	private static class SelectMenuItem implements Serializable {
		private static final long serialVersionUID = 1L;

		private String id;
		private String text;
		private String icon;
		private boolean disabled;
		@JsonIgnore
		private transient EventHandler handler;

		public SelectMenuItem(String id, String text, boolean disabled, String icon, EventHandler handler) {
			this.id = id;
			this.text = text;
			this.icon = icon;
			this.disabled = disabled;
			this.handler = handler;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}

		public EventHandler getHandler() {
			return handler;
		}

		public void setHandler(EventHandler handler) {
			this.handler = handler;
		}
	}
}
