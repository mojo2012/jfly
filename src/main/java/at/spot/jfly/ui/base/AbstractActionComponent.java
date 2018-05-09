package at.spot.jfly.ui.base;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.display.Image;
import at.spot.jfly.util.Localizable;

public class AbstractActionComponent extends AbstractLabelledComponent {
	private boolean expandHorizontally = false;
	private Image image;

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
}
