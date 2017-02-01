package at.spot.jfly.ui.selection;

import java.util.ArrayList;
import java.util.List;

import at.spot.jfly.style.ButtonStyle;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.action.LinkAction;

public class SingleButtonDropDown extends Button {

	final private List<LinkAction> menuItems = new ArrayList<>();

	public SingleButtonDropDown(String text) {
		super(text);
		componentType(ComponentType.Button);
		addStyleClasses(ButtonStyle.Success);
	}

	public SingleButtonDropDown addMenuItem(LinkAction menuItem) {
		menuItems.add(menuItem);
		return this;
	}

	public SingleButtonDropDown removeMenuItem(LinkAction menuItem) {
		menuItems.remove(menuItem);
		return this;
	}

	public List<LinkAction> menuItems() {
		return menuItems;
	}

}
