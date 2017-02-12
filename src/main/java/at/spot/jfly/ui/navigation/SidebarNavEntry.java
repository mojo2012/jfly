package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractActionComponent;

public class SidebarNavEntry extends AbstractActionComponent {

	private boolean selected = false;

	public SidebarNavEntry(ComponentHandler handler, String text) {
		super(handler, text);
	}

	public <C extends SidebarNavEntry> C selected(boolean selected) {
		this.selected = selected;
		return (C) this;
	}

	public boolean selected() {
		return this.selected;
	}
}
