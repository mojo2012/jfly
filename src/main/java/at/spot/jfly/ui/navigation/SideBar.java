package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class SideBar extends AbstractContainerComponent<AbstractComponent> {

	private boolean minimized = false;
	private boolean permanent = false;

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

	public boolean isPermanent() {
		return this.permanent;
	}

	public void setPermanent(boolean isPermanent) {
		this.permanent = isPermanent;
	}
}
