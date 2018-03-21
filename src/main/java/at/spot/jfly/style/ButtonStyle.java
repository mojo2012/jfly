package at.spot.jfly.style;

/**
 * All possible predefined styles for buttons.
 */
public enum ButtonStyle implements Modifier {
	None(""),
	Default("w3-black"),
	Primary("w3-teal"),
	Success("w3-green"),
	Info("w3-blue"),
	Warning("w3-orange"),
	Danger("w3-redr"),
	Link("");

	private String styleClass;

	private ButtonStyle(final String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public String getName() {
		return this.styleClass;
	}
}
