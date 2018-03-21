package at.spot.jfly.ui.action;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.style.ButtonStyle;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.ui.base.AbstractActionComponent;
import at.spot.jfly.ui.display.Icon;
import at.spot.jfly.util.Localizable;

public class Button extends AbstractActionComponent {

	private Icon icon;
	private boolean isFlat;

	public Button(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
		componentType(ComponentType.Button);
		addStyleClasses(ButtonStyle.None);
	}

	public Button(final ComponentHandler handler) {
		super(handler, null);
		componentType(ComponentType.Button);
		addStyleClasses(ButtonStyle.None);
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public boolean isFlat() {
		return isFlat;
	}

	public void setFlat(boolean isFlat) {
		this.isFlat = isFlat;
	}

}
