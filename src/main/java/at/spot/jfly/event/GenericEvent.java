package at.spot.jfly.event;

public enum GenericEvent implements EventType {
	stateChanged("stateChanged");
	private String identifier;

	private GenericEvent(final String id) {
		this.identifier = id;
	}

	@Override
	public String toString() {
		return this.identifier;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}
}
