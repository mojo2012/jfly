package at.spot.jfly.ui.base;

import at.spot.jfly.event.Event;

public interface EventTarget {
	/**
	 * Pass an event for the component to process.
	 */
	<E extends Event> void handleEvent(final E event);

}
