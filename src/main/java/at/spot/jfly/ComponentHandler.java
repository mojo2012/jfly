package at.spot.jfly;

import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.Component;

public interface ComponentHandler {
	void registerComponent(final Component component);

	String renderComponent(final AbstractComponent component);
}
