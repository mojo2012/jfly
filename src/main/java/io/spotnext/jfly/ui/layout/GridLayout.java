package io.spotnext.jfly.ui.layout;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Attributes.GridBehavior;
import io.spotnext.jfly.attributes.Attributes.GridGutterSize;
import io.spotnext.jfly.attributes.Attributes.GridLayoutSize;
import io.spotnext.jfly.ui.base.AbstractLayoutComponent;

public class GridLayout extends AbstractLayoutComponent {

	private GridLayoutSize size = GridLayoutSize.XS6;
	private GridGutterSize gutterSize = GridGutterSize.GridListXL;
	private GridBehavior behavior = GridBehavior.JustifyCenter;

	public GridLayout(final ComponentHandler handler) {
		super(handler);
	}

	public GridLayout(final ComponentHandler handler, GridBehavior behavior, GridLayoutSize size,
			GridGutterSize gutterSize) {

		super(handler);

		if (size != null) {
			this.size = size;
		}

		if (gutterSize != null) {
			this.gutterSize = gutterSize;
		}

		if (behavior != null) {
			this.behavior = behavior;
		}
	}

	public GridLayoutSize getSize() {
		return size;
	}

	public GridGutterSize getGutterSize() {
		return gutterSize;
	}

	public GridBehavior getBehavior() {
		return behavior;
	}

}
