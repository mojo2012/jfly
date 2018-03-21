package at.spot.jfly.layout;

import at.spot.jfly.style.Modifier;

public enum Direction implements Modifier {
	Up("up"), Down("down"), Right("right"), Left("left");

	private String internal;

	private Direction(final String internal) {
		this.internal = internal;
	}

	@Override
	public String getName() {
		return this.internal;
	}
}
