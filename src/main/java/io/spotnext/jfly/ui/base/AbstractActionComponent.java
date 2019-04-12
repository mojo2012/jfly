package io.spotnext.jfly.ui.base;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.display.Image;
import io.spotnext.jfly.util.Localizable;

public class AbstractActionComponent extends AbstractLabelledComponent {
	private boolean expandHorizontally = false;
	private Image image;
	private boolean enabled = true;

	public AbstractActionComponent(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
	}

	public boolean isExpandHorizontally() {
		return expandHorizontally;
	}

	public void setExpandHorizontally(boolean expandHorizontally) {
		this.expandHorizontally = expandHorizontally;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return this.image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		updateClientComponent();
	}

}
