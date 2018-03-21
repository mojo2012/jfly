package at.spot.jfly.style;

/**
 * All possible predefined styles for labels.
 */
public enum BadgeStyle implements Modifier {
	None("w3-black"),
	White("w3-white"),
	Green("w3-green"),
	Red("w3-red"),
	Blue("w3-blue"),
	Turquoise("w3-turquoise"),
	LightGreen("w3-metro-light-green"),;

	private String styleClass;

	private BadgeStyle(final String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public String getName() {
		return this.styleClass;
	}

}
