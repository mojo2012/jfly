package at.spot.jfly.ui.base;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.util.Localizable;

public class AbstractActionComponent extends AbstractLabelledComponent {
	private boolean expandHorizontally = false;

	public AbstractActionComponent(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}

	public boolean isExpandHorizontally() {
		return expandHorizontally;
	}

	public void setExpandHorizontally(boolean expandHorizontally) {
		this.expandHorizontally = expandHorizontally;
	}
}
