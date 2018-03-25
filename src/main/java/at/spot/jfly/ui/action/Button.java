package at.spot.jfly.ui.action;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Attributes;
import at.spot.jfly.attributes.Styles;
import at.spot.jfly.ui.base.AbstractActionComponent;
import at.spot.jfly.ui.display.Icon;
import at.spot.jfly.util.Localizable;

public class Button extends AbstractActionComponent {

	private Icon icon;
	private boolean expandFullWidth = false;
	private Styles.Color color;
	private Attributes.TextFieldType type;

	public Button(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}

	public Button(final ComponentHandler handler) {
		super(handler, null);
	}

	public Attributes.TextFieldType getType() {
		return type;
	}

	public void setType(Attributes.TextFieldType type) {
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

	public boolean isExpandFullWidth() {
		return expandFullWidth;
	}

	public void setExpandFullWidth(boolean expandFullWidth) {
		this.expandFullWidth = expandFullWidth;
	}

}
