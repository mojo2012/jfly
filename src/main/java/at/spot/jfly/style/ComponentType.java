package at.spot.jfly.style;

/**
 * All general predefined styles.
 */
public enum ComponentType implements Modifier {
	Button("button"), Label("label"), Badge("badge"), NavBar("navbar");

	private String styleClass;

	private ComponentType(final String internal) {
		this.styleClass = internal;
	}

	@Override
	public String getName() {
		return this.styleClass;
	}
}
