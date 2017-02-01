package at.spot.jfly.ui.html;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractComponent;

public class Script extends AbstractComponent {

	private String location;

	public Script(final ComponentHandler handler, final String scriptLocation) {
		super(handler);
		this.location(scriptLocation);
	}

	public String location() {
		return location;
	}

	public Script location(final String location) {
		this.location = location;

		return this;
	}
}