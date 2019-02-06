package io.spotnext.jfly.ui.generic;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;
import j2html.TagCreator;
import j2html.tags.ContainerTag;

public class HtmlTag extends AbstractComponent {
	private final transient ContainerTag raw;

	/**
	 * Use {@link TagCreator} to create tags in a conveinient manner.
	 */
	public HtmlTag(final ComponentHandler handler, final ContainerTag tag) {
		super(handler);
		this.raw = tag;
	}

	@Override
	public String render() {
		return raw.render();
	}
}