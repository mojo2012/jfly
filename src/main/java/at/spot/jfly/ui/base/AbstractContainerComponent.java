package at.spot.jfly.ui.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import at.spot.jfly.ComponentHandler;
import j2html.tags.ContainerTag;

public abstract class AbstractContainerComponent extends AbstractComponent {

	protected final transient List<Component> children = new ArrayList<>();

	public AbstractContainerComponent(final ComponentHandler handler) {
		super(handler);
	}

	public List<Component> children() {
		return Collections.unmodifiableList(children);
	}

	public <C extends AbstractContainerComponent> C addChildren(final ContainerTag... tags) {
		final Component[] comps = Arrays.stream(tags).map((t) -> new GenericComponent(handler(), t))
				.collect(Collectors.toList()).toArray(new Component[0]);

		addChildren(comps);
		return (C) this;
	}

	public <C extends AbstractContainerComponent> C addChildren(final Component... components) {
		children.addAll(Arrays.asList(components));

		for (Component c : components) {
			updateClient("jfly", "addChildComponent", this.uuid(), c.render());
		}

		return (C) this;
	}

	public <C extends AbstractContainerComponent> C removeChildren(final Component... components) {
		children.removeAll(Arrays.asList(components));

		for (Component c : components) {
			updateClient("jfly", "removeChildComponent", this.uuid(), c.uuid());
		}

		return (C) this;
	}

}
