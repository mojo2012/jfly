package at.spot.jfly.ui.base;

import at.spot.jfly.event.DomEvent;

public interface EventTarget {
	/**
	 * Pass an event for the component to process.
	 */
	<E extends DomEvent> void handleEvent(final E event);

}
