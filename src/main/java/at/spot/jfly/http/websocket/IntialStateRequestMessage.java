package at.spot.jfly.http.websocket;

import java.util.Locale;

public class IntialStateRequestMessage extends Message {
	private Locale currentLocale;

	public IntialStateRequestMessage() {
		super(MessageType.initialStateRequest);
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}


}
