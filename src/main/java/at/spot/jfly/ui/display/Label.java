package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractLabelledComponent;
import at.spot.jfly.util.Localizable;

/**
 * Implements a bootstrap label:<br>
 * <br>
 * <button type="button" class="btn btn-default">Default</button>
 */
public class Label extends AbstractLabelledComponent {
	public Label(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}

	public Label(final ComponentHandler handler) {
		super(handler);
	}
}
