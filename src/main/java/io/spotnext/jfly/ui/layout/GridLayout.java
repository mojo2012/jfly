package io.spotnext.jfly.ui.layout;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Attributes.GridLayoutSize;
import io.spotnext.jfly.ui.base.AbstractLayoutComponent;

public class GridLayout extends AbstractLayoutComponent {

	private GridLayoutSize size = GridLayoutSize.XS6;

	public GridLayout(final ComponentHandler handler) {
		super(handler);
	}

	public GridLayoutSize getSize() {
		return size;
	}

	public void setSize(GridLayoutSize size) {
		this.size = size;
		updateClientComponent();
	}

}
