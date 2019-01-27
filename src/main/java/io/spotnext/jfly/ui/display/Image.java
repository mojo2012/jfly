package io.spotnext.jfly.ui.display;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

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
