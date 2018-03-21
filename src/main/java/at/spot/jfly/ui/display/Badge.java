package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractTextComponent;
import at.spot.jfly.util.Localizable;

/**
 * Implements a bootstrap badge:<br>
 * <br>
 * <button type="button" class="btn btn-default">Default</button>
 */
public class Badge extends AbstractTextComponent {
	public Badge(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
		addStyleClasses("badge");
	}
}
