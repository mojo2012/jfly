package io.spotnext.jfly;

import java.util.Map;

import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.ui.base.Component;

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
	 * @see ComponentHandler#renderComponent(AbstractComponent)
	 * 
	 * @param additionalValues
	 *            additional values that are passed to the render service
	 */
	String renderComponent(AbstractComponent component, Map<String, Object> additionalValues);

	/**
	 * Sends all client updates immediately back to the browser.
	 */
	void flushClientUpdates(Component component);

	/**
	 * Shows and error message in the client.
	 */
	void showErrorMessage(String message);

}
