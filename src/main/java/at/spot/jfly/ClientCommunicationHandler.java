package at.spot.jfly;

import at.spot.jfly.http.websocket.Message;

public interface ClientCommunicationHandler {
	/*
	 * Send a message to the client of the current session.
	 */
	<M extends Message> void sendMessage(M message);

	/**
	 * Send a message to the client with the given session id.
	 * 
	 * @param clientSessionId
	 * @param message
	 */
	<M extends Message> void sendMessage(String clientSessionId, M message);
}
