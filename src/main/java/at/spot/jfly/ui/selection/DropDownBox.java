package at.spot.jfly.ui.selection;

import java.util.Map;
import java.util.TreeMap;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.MaterialIcon;
import at.spot.jfly.util.Localizable;

public class DropDownBox extends Menu {
	final private Map<String, SelectMenuItem> menuItems = new TreeMap<>();

	private MaterialIcon leftIcon;
	private MaterialIcon rightIcon;
	private boolean editable = false;

	public DropDownBox(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
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

}
