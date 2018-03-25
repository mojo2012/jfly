package at.spot.jfly.http.websocket;

public class KeepAliveMessage extends Message {

	public KeepAliveMessage() {
		super(MessageType.keepAlive);
	}

}
