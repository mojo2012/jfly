package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.style.BadgeStyle;
import at.spot.jfly.ui.base.AbstractTextComponent;

/**
 * Implements a bootstrap badge:<br>
 * <br>
 * <button type="button" class="btn btn-default">Default</button>
 */
public class Badge extends AbstractTextComponent {
	public Badge(final ComponentHandler handler, final String text) {
		super(handler, text);
		addStyleClasses(BadgeStyle.None);
	}
}
