package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractContainerComponent;

public class TreeView extends AbstractContainerComponent<TreeNode> {

	public TreeView(ComponentHandler handler) {
		super(handler);
	}

	public enum NodeType {
		SPLITTER, DEFAULT, SUB_HEADER
	}
}
