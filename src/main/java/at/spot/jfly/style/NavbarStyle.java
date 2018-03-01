package at.spot.jfly.style;

/**
 * ToolBar related styles.
 */
public enum NavbarStyle implements Style {
	Default("navbar-default"), Inverse("navbar-inverse"), NavBarHeader("navbar-header"), NavBarContent(
			"nav navbar-nav"), NavBarHeaderBrand("navbar-brand");

	private String styleClass;

	private NavbarStyle(final String internal) {
		this.styleClass = internal;
	}

	@Override
	public String internalName() {
		return this.styleClass;
	}
}
