package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractActionComponent;

public class SidebarNavEntry extends AbstractActionComponent {
	private boolean selected = false;

	public SidebarNavEntry(ComponentHandler handler, String text) {
		super(handler, text);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}
}
