package io.spotnext.jfly.ui.selection;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.MaterialIcon;
import io.spotnext.jfly.util.Localizable;

public class DropDownBox extends Menu {
	private MaterialIcon leftIcon;
	private MaterialIcon rightIcon;
	private boolean editable = false;

	public DropDownBox(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
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

}
