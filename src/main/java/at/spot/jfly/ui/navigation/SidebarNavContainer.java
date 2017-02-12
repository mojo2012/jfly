package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class SidebarNavContainer extends AbstractContainerComponent<SidebarNavEntry> {

	private String title;

	public SidebarNavContainer(ComponentHandler handler) {
		super(handler);
	}

	public String title() {
		return title;
	}

	public <S extends SidebarNavContainer> S title(String title) {
		this.title = title;
		return (S) this;
	}
}
