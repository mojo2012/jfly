package io.spotnext.jfly.ui.display;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractLabelledComponent;
import io.spotnext.jfly.util.Localizable;

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
