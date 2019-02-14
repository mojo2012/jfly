package io.spotnext.jfly.ui.generic;

import io.spotnext.jfly.ComponentHandler;

public class SimpleHtmlTag extends AbstractHtmlTag {

	private String content;

	public SimpleHtmlTag(final ComponentHandler handler, String tagName) {
		super(handler, tagName);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		updateClientComponent();
	}

}
