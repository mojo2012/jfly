package io.spotnext.jfly.ui.display;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractLabelledComponent;
import io.spotnext.jfly.util.Localizable;

public class Badge extends AbstractLabelledComponent {
	public Badge(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}
}
