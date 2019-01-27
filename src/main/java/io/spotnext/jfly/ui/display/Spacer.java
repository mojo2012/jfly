package io.spotnext.jfly.ui.display;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

public class Spacer extends AbstractComponent {

	public Spacer(ComponentHandler handler) {
		super(handler);
	}

	// @ExposeMethodResult("isSpacer")
	public boolean isSpacer() {
		return true;
	}
}
