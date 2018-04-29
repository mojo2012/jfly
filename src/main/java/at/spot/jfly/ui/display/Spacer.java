package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractComponent;

public class Spacer extends AbstractComponent {

	public Spacer(ComponentHandler handler) {
		super(handler);
	}

	// @ExposeMethodResult("isSpacer")
	public boolean isSpacer() {
		return true;
	}
}
