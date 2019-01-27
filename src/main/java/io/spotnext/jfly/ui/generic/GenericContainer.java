package io.spotnext.jfly.ui.generic;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.ui.base.AbstractContainerComponent;

public class GenericContainer extends AbstractContainerComponent<AbstractComponent> {

	private String tagName;
	private boolean useWrapper = false;

	public GenericContainer(ComponentHandler handler, String tagName) {
		super(handler);
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

	public boolean isUseWrapper() {
		return useWrapper;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Indicates that the renderer should create an additional wrapper element
	 * around the child components.
	 */
	public void setUseWrapper(boolean renderWrapper) {
		this.useWrapper = renderWrapper;
	}

}
