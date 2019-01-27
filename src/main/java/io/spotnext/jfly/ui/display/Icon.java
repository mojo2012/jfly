package io.spotnext.jfly.ui.display;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.MaterialIcon;
import io.spotnext.jfly.ui.base.AbstractComponent;

public class Icon extends AbstractComponent {
	private MaterialIcon icon;
	private boolean inverse = false;

	public boolean isInverse() {
		return inverse;
	}

	public MaterialIcon getIcon() {
		return icon;
	}

	public void setIcon(MaterialIcon icon) {
		this.icon = icon;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	public Icon(final ComponentHandler handler, MaterialIcon icon) {
		super(handler);
		this.icon = icon;
	}
}
