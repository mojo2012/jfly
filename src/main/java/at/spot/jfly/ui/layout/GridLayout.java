package at.spot.jfly.ui.layout;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Attributes.GridLayoutSize;
import at.spot.jfly.ui.base.AbstractLayoutComponent;

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
