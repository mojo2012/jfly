package io.spotnext.jfly.ui.base;

import io.spotnext.jfly.event.DomEvent;

public interface EventTarget {
	/**
	 * Pass an event for the component to process.
	 */
	<E extends DomEvent> void handleEvent(final E event);

}
