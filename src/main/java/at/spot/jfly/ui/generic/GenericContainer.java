package at.spot.jfly.ui.generic;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class GenericContainer extends AbstractContainerComponent<AbstractComponent> {

	private String tagName;

	public GenericContainer(ComponentHandler handler, String tagName) {
		super(handler);
		this.tagName = tagName;
	}

	public String tagName() {
		return tagName;
	}

}
