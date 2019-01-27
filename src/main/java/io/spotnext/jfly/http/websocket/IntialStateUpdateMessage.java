package io.spotnext.jfly.http.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.spotnext.jfly.ui.base.Component;
import io.spotnext.jfly.util.KeyValueMapping;

public class IntialStateUpdateMessage extends Message {
	private final KeyValueMapping<String, Component> componentStates = new KeyValueMapping<>();
	private final KeyValueMapping<String, Object> globalState = new KeyValueMapping<>();
	private final List<Locale> supportedLocales = new ArrayList<>();
	private Locale currentLocale;

	public IntialStateUpdateMessage() {
		super(MessageType.initialStateUpdate);
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public KeyValueMapping<String, Component> getComponentStates() {
		return componentStates;
	}

	public KeyValueMapping<String, Object> getGlobalState() {
		return globalState;
	}

	public List<Locale> getSupportedLocales() {
		return supportedLocales;
	}

}
