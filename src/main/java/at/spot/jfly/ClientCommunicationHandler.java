package at.spot.jfly;

public interface ClientCommunicationHandler {
	/*
	 * Send a message to the client of the current session.
	 */
	void sendMessage(Object message);

	/**
	 * Send a message to the client with the given session id.
	 * 
	 * @param clientSessionId
	 * @param message
	 */
	void sendMessage(String clientSessionId, Object message);
}
