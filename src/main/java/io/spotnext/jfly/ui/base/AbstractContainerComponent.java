package io.spotnext.jfly.ui.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Styles.Color;
import io.spotnext.jfly.util.Container;

public abstract class AbstractContainerComponent<C extends AbstractComponent> extends AbstractComponent
		implements Container<C> {

	protected final transient List<Component> children = new ArrayList<>();
	private Color color;

	public AbstractContainerComponent(final ComponentHandler handler) {
		super(handler);
	}

	public List<C> getChildren() {
		return (List<C>) Collections.unmodifiableList(children);
	}

	@Override
	public void addChildren(final C... components) {
		children.addAll(Arrays.asList(components));

		for (Component c : components) {
			updateClient(ComponentManipulationFunction.ADD, this.getUuid(), c.render(), c);
		}
	}

	public void removeChildren(final C... components) {
		children.removeAll(Arrays.asList(components));

		for (Component c : components) {
			updateClient(ComponentManipulationFunction.REMOVE, this.getUuid(), c.getUuid());
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
