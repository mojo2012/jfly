package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class SideBar extends AbstractContainerComponent<SidebarNavContainer> {

	private boolean minimized = false;

	public SideBar(final ComponentHandler handler) {
		super(handler);
	}

	public boolean isMinimized() {
		return minimized;
	}

	public void setMinimized(boolean minimized) {
		this.minimized = minimized;
		updateClientComponent();
	}

}
