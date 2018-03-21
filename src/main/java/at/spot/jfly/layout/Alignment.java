package at.spot.jfly.layout;

import at.spot.jfly.style.Modifier;

public enum Alignment implements Modifier {
	Top("top"), Bottom("bottom"), Right("right"), Left("left"), Center("center");

	private String internal;

	private Alignment(final String internal) {
		this.internal = internal;
	}

	@Override
	public String getName() {
		return this.internal;
	}
}
