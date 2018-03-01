package at.spot.jfly.style;

public enum NavigationTarget {
	Blank("_blank"), Self("_self"), Parent("_parent"), Top("_top");

	private String internName;

	private NavigationTarget(final String internName) {
		this.internName = internName;
	}

	public String internalName() {
		return this.internName;
	}

	@Override
	public String toString() {
		return internalName();
	}
}
