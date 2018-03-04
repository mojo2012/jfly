package at.spot.jfly.ui.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.spot.jfly.ComponentHandler;

public abstract class AbstractContainerComponent<C extends AbstractComponent> extends AbstractComponent {

	protected final transient List<Component> children = new ArrayList<>();

	public AbstractContainerComponent(final ComponentHandler handler) {
		super(handler);
	}

	public List<Component> children() {
		return Collections.unmodifiableList(children);
	}

	// public <S extends AbstractContainerComponent> S addChildren(final
	// ContainerTag... tags) {
	// final Component[] comps = Arrays.stream(tags).map((t) -> new
	// GenericComponent(handler(), t))
	// .collect(Collectors.toList()).toArray(new Component[0]);
	//
	// addChildren(comps);
	// return (S) this;
	// }

	public <S extends AbstractContainerComponent<C>> S addChildren(final C... components) {
		children.addAll(Arrays.asList(components));

		for (Component c : components) {
			updateClient("jfly", "addChildComponent", this.uuid(), c.render(), c);
		}

		return (S) this;
	}

	public <S extends AbstractContainerComponent<C>> S removeChildren(final C... components) {
		children.removeAll(Arrays.asList(components));

		for (Component c : components) {
			updateClient("jfly", "removeChildComponent", this.uuid(), c.uuid());
		}

		return (S) this;
	}

}
