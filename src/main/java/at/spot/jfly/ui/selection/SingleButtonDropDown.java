package at.spot.jfly.ui.selection;

import java.util.ArrayList;
import java.util.List;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.style.ButtonStyle;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.action.LinkAction;

public class SingleButtonDropDown extends Button {

	final private List<LinkAction> menuItems = new ArrayList<>();

	public SingleButtonDropDown(final ComponentHandler handler, final String text) {
		super(handler, text);
		componentType(ComponentType.Button);
		addStyleClasses(ButtonStyle.None);
	}

	public SingleButtonDropDown addMenuItem(final LinkAction menuItem) {
		menuItems.add(menuItem);
		return this;
	}

	public SingleButtonDropDown removeMenuItem(final LinkAction menuItem) {
		menuItems.remove(menuItem);
		return this;
	}

	public List<LinkAction> menuItems() {
		return menuItems;
	}

}
