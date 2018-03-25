package at.spot.jfly.http.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.spot.jfly.ui.base.Component;

public class ComponentStateUpdateMessage extends Message {
	private String componentUuid;
	private Component component;

	public ComponentStateUpdateMessage() {
		super(MessageType.componentStateUpdate);
	}

	public String getComponentUuid() {
		return componentUuid;
	}

	public void setComponent(Component component) {
		this.component = component;
		this.componentUuid = component.getUuid();
	}

	@JsonProperty("componentState")
	public Component getComponent() {
		return component;
	}

}
