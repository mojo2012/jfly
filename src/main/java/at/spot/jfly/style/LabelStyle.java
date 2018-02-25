package at.spot.jfly.style;

/**
 * All possible predefined styles for labels.
 */
public enum LabelStyle implements Style {
	None(""),
	Default("w3-black"),
	Primary("w3-teal"),
	Success("w3-green"),
	Info("w3-blue"),
	Warning("w3-orange"),
	Danger("w3-red"),;

	private String styleClass;

	private LabelStyle(final String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public String internalName() {
		return this.styleClass;
	}
}
