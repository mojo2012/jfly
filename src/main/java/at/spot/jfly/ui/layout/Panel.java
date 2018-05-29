package at.spot.jfly.ui.layout;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Attributes.GridLayoutSize;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class Panel extends AbstractContainerComponent<AbstractComponent> {

	private GridLayoutSize gridSize;

	public Panel(ComponentHandler handler) {
		super(handler);
	}

	public GridLayoutSize getGridSize() {
		return gridSize;
	}

	public void setGridSize(GridLayoutSize gridSize) {
		this.gridSize = gridSize;
		updateClientComponent();
	}

}
