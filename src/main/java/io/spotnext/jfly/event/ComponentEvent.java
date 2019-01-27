package io.spotnext.jfly.event;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.spotnext.jfly.event.Events.EventType;

public class ComponentEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "eventName")
	protected EventType eventType;

	public EventType getEventType() {
		return eventType;
	}

}
