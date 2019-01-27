package io.spotnext.jfly.ui.layout;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

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
		this.left.addStyleClass("column-left");
		updateClient(ComponentManipulationFunction.ADD, left);
	}

	public Panel getRight() {
		return right;
	}

	public void setRight(Panel right) {
		this.right = right;
		this.right.addStyleClass("column-right");
		updateClient(ComponentManipulationFunction.ADD, right);
	}

}
