package at.spot.jfly.style;

/**
 * All possible predefined styles for horizontal orientation.
 */
public enum HorizontalOrientation implements Style {
	Left("left"), Right("right");

	private String styleClass;

	private HorizontalOrientation(final String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public String internalName() {
		return this.styleClass;
	}

	@Override
	public String toString() {
		return internalName();
	}
}
