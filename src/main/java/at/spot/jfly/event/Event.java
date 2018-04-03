package at.spot.jfly.event;

import java.util.Map;

import at.spot.jfly.event.Events.EventType;
import at.spot.jfly.ui.base.Component;

public class Event {
	private final EventType eventType;
	private final Component source;
	private final Map<String, Object> payload;

	public Event(final EventType eventType, final Component source, final Map<String, Object> payload) {
		this.source = source;
		this.eventType = eventType;
		this.payload = payload;
	}

	public Component getSource() {
		return this.source;
	}

	public EventType getEventType() {
		return eventType;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}
}
