package at.spot.jfly.ui.selection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.MaterialIcon;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.ui.base.AbstractLabelledComponent;
import at.spot.jfly.util.Localizable;

public class Menu extends AbstractLabelledComponent {
	final private Map<String, SelectMenuItem> menuItems = new TreeMap<>();

	private MaterialIcon leftIcon;
	private MaterialIcon rightIcon;
	private boolean editable = false;
	private SelectMenuItem selectedItem;

	public Menu(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}

	public void addMenuItem(final String itemId, String text, final EventHandler handler) {
		SelectMenuItem item = menuItems.get(itemId);

		// remove previously added item with the same id -> unregisters event handler
		if (item != null) {
			removeMenuItem(itemId);
		}

		menuItems.put(itemId, new SelectMenuItem(itemId, text, false, null, handler));
		updateClientComponent();
	}

	public String getSelectedItemId() {
		return selectedItem != null ? selectedItem.id : null;
	}

	public SelectMenuItem getSelectedItem() {
		return selectedItem;
	}

	public void selectedItem(String itemId) {
		selectedItem = menuItems.get(itemId);
		updateClientComponent();
	}

	public void removeMenuItem(final String itemId) {
		menuItems.get(itemId);
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public MaterialIcon getLeftIcon() {
		return leftIcon;
	}

	public void setLeftIcon(MaterialIcon leftIcon) {
		this.leftIcon = leftIcon;
	}

	public MaterialIcon getRightIcon() {
		return rightIcon;
	}

	public void setRightIcon(MaterialIcon rightIcon) {
		this.rightIcon = rightIcon;
	}

	public Collection<SelectMenuItem> getMenuItems() {
		return menuItems.values();
	}

	protected class SelectMenuItem implements Serializable {
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
