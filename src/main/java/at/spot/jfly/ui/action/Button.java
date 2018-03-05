package at.spot.jfly.ui.action;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.style.ButtonStyle;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.ui.base.AbstractActionComponent;
import at.spot.jfly.ui.display.Icon;

public class Button extends AbstractActionComponent {

	private Icon icon;

	public Button(final ComponentHandler handler, final String text) {
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


}
