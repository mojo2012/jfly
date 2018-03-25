package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractTextComponent;
import at.spot.jfly.util.Localizable;

/**
 * Implements a bootstrap label:<br>
 * <br>
 * <button type="button" class="btn btn-default">Default</button>
 */
public class Label extends AbstractTextComponent {
	public Label(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}
}
