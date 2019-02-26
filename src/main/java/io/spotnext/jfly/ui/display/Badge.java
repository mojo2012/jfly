package io.spotnext.jfly.ui.display;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Styles.Color;
import io.spotnext.jfly.ui.base.AbstractLabelledComponent;
import io.spotnext.jfly.util.Localizable;

public class Badge extends AbstractLabelledComponent {
	private Color color = Color.RED;

	public Badge(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}

	public Badge(final ComponentHandler handler) {
		super(handler, Localizable.of(""));
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		updateClientComponent();
	}

}
