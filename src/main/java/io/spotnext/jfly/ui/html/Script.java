package io.spotnext.jfly.ui.html;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

public class Script extends AbstractComponent {

	private String location;

	public Script(final ComponentHandler handler, final String scriptLocation) {
		super(handler);
		this.setLocation(scriptLocation);
	}

	public String getLocation() {
		return location;
	}

	public Script setLocation(final String location) {
		this.location = location;

		return this;
	}
}