package io.spotnext.jfly.ui.action;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Attributes;
import io.spotnext.jfly.attributes.Styles;
import io.spotnext.jfly.ui.base.AbstractActionComponent;
import io.spotnext.jfly.ui.display.Icon;
import io.spotnext.jfly.util.Localizable;

public class Button extends AbstractActionComponent {

	private Icon icon;
	private Styles.Color color;
	private Attributes.TextFieldStyle type;

	public Button(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}

	public Button(final ComponentHandler handler) {
		super(handler, null);
	}

	public Attributes.TextFieldStyle getType() {
		return type;
	}

	public void setType(Attributes.TextFieldStyle type) {
		this.type = type;
	}

	public Styles.Color getColor() {
		return color;
	}

	public void setColor(Styles.Color color) {
		this.color = color;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

}
