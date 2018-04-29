package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractLabelledComponent;
import at.spot.jfly.util.Localizable;

public class Badge extends AbstractLabelledComponent {
	public Badge(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}
}
