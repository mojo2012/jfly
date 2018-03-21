package at.spot.jfly.style;

/**
 * All possible predefined styles for horizontal orientation.
 */
public enum HorizontalOrientation implements Modifier {
	Left("left"), Right("right");

	private String styleClass;

	private HorizontalOrientation(final String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public String getName() {
		return this.styleClass;
	}
}
