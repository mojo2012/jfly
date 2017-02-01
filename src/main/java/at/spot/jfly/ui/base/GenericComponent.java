package at.spot.jfly.ui.base;

import at.spot.jfly.ComponentHandler;
import j2html.tags.ContainerTag;

public class GenericComponent extends AbstractComponent {
	private final transient ContainerTag raw;

	public GenericComponent(final ComponentHandler handler, final ContainerTag tag) {
		super(handler);
		this.raw = tag;
	}

	@Override
	public String render() {
		return raw.render();
	}
}
