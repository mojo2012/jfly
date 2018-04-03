package at.spot.jfly.http.websocket;

import at.spot.jfly.event.Events.EventType;
import at.spot.jfly.util.KeyValueMapping;

public class EventMessage extends Message {

	private EventType eventType;
	private String componentUuid;
	private KeyValueMapping<String, Object> payload;

	protected EventMessage() {
		super(MessageType.event);
	}

	public KeyValueMapping<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(KeyValueMapping<String, Object> payload) {
		this.payload = payload;
	}

	public EventMessage(MessageType type) {
		super(type);
	}

	public String getComponentUuid() {
		return componentUuid;
	}

	public void setComponentUuid(String componentUuid) {
		this.componentUuid = componentUuid;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}
