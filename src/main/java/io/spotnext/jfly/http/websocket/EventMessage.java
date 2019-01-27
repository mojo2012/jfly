package io.spotnext.jfly.http.websocket;

import io.spotnext.jfly.event.DomEvent;
import io.spotnext.jfly.event.Events.EventType;
import io.spotnext.jfly.util.KeyValueMapping;

public class EventMessage extends Message {

	private EventType eventType;
	private String componentUuid;
	private DomEvent domEventData;
	private KeyValueMapping<String, Object> componentState;

	protected EventMessage() {
		super(MessageType.event);
	}

	public EventMessage(MessageType type) {
		super(type);
	}

	public DomEvent getDomEventData() {
		return domEventData;
	}

	public void setDomEventData(DomEvent eventData) {
		this.domEventData = eventData;
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

	public KeyValueMapping<String, Object> getComponentState() {
		return componentState;
	}

	public void setComponentState(KeyValueMapping<String, Object> componentState) {
		this.componentState = componentState;
	}

}
