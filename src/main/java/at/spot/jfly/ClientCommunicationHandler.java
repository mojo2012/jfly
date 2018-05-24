package at.spot.jfly;

import javax.servlet.http.HttpSession;

import at.spot.jfly.http.websocket.Message;

public interface ClientCommunicationHandler {
	/**
	 * Send a message to the client of the current session.
	 */
	<M extends Message> void sendMessage(M message);

	/**
	 * Send a message to the client with the given session id.
	 */
	<M extends Message> void sendMessage(M message, String sessionId);

	/**
	 * Sends an error message to the client.
	 */
	void sendErrorMessage(String message, Throwable exception);

	/**
	 * Return the current web session.
	 */
	HttpSession getCurrentHttpSession();
}
