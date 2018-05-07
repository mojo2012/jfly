package at.spot.jfly.ui.display;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractComponent;

public class Image extends AbstractComponent {
	private String sourceUrl;

	public Image(final ComponentHandler handler, String sourceUrl) {
		super(handler);
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		updateClientComponent();
	}

}
