package at.spot.jfly.ui.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.spot.jfly.ComponentHandler;

public abstract class AbstractContainerComponent<C extends AbstractComponent> extends AbstractComponent {

	protected final transient List<Component> children = new ArrayList<>();

	public AbstractContainerComponent(final ComponentHandler handler) {
		super(handler);
	}

	public List<Component> getChildren() {
		return Collections.unmodifiableList(children);
	}

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

	@JsonProperty(value = "hasChildren")
	// @ExposeMethodResult("hasChildren")
	public boolean hasChildren() {
		return children.size() > 0;
	}
}
