package at.spot.jfly;

import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.Component;

public interface ComponentHandler {
	/**
	 * Registers a component in the component handler.
	 */
	void registerComponent(final Component component);

	/**
	 * Returns the rendered HTML of the given component.
	 */
	String renderComponent(final AbstractComponent component) throws IllegalStateException;

	/**
	 * Sends all client updates immediately back to the browser.
	 */
	void flushClientUpdates(Component component);
}
