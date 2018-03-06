package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class SidebarNavContainer extends AbstractContainerComponent<SidebarNavEntry> {

	private String title;

	public SidebarNavContainer(ComponentHandler handler) {
		super(handler);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
