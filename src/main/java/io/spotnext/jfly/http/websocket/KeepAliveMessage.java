package io.spotnext.jfly.http.websocket;

public class KeepAliveMessage extends Message {

	public KeepAliveMessage() {
		super(MessageType.keepAlive);
	}

}
