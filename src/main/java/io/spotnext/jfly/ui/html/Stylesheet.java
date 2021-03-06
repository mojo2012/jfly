package io.spotnext.jfly.ui.html;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

public class Stylesheet extends AbstractComponent {

	private String location;

	public Stylesheet(final ComponentHandler handler, final String styleLocation) {
		super(handler);
		setLocation(styleLocation);
	}

	public String getLocation() {
		return location;
	}

	public Stylesheet setLocation(final String location) {
		this.location = location;

		return this;
	}
}
