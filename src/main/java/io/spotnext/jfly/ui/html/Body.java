package io.spotnext.jfly.ui.html;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.event.EventHandler;
import io.spotnext.jfly.event.Events.JsEvent;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.ui.base.AbstractContainerComponent;

public class Body extends AbstractContainerComponent<AbstractComponent> {

	public Body(final ComponentHandler handler) {
		super(handler);
	}

	public void onLocationChange(final EventHandler handler) {
		onEvent(JsEvent.Load, handler);
		onEvent(JsEvent.PopState, handler);

		// TODO bug in vue? hashchange is not working properly
		onEvent(JsEvent.HashChange, handler);
	}

}