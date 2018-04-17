package at.spot.jfly.http.websocket;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = IntialStateUpdateMessage.class, name = "initialStateUpdate"),
		@Type(value = IntialStateRequestMessage.class, name = "initialStateRequest"),
		@Type(value = ComponentManipulationMessage.class, name = "componentManipulation"),
		@Type(value = ComponentStateUpdateMessage.class, name = "componentStateUpdate"),
		@Type(value = FunctionCallMessage.class, name = "functionCall"),
		@Type(value = EventMessage.class, name = "event"), @Type(value = KeepAliveMessage.class, name = "keepAlive"),
		@Type(value = ExceptionMessage.class, name = "exception"), })
public class Message {
	private MessageType type;
	private String sessionId;
	private String url;

	public Message() {
	}

	public Message(MessageType type) {
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public enum MessageType {
		initialStateRequest,
		initialStateUpdate,
		keepAlive,
		exception,
		event,
		notification,
		componentStateUpdate,
		stateRequest,
		componentManipulation,
		functionCall
	}
}
