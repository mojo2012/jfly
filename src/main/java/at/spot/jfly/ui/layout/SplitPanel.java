package at.spot.jfly.ui.layout;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractComponent;

public class SplitPanel extends AbstractComponent {

	private Panel left;
	private Panel right;

	public SplitPanel(ComponentHandler handler) {
		super(handler);
	}

	public Panel getLeft() {
		return left;
	}

	public void setLeft(Panel left) {
		this.left = left;
		updateClient(ComponentManipulationFunction.ADD, left);
	}

	public Panel getRight() {
		return right;
	}

	public void setRight(Panel right) {
		this.right = right;
		updateClient(ComponentManipulationFunction.ADD, right);
	}

}
