package io.spotnext.jfly.ui.navigation;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractContainerComponent;

public class TreeView extends AbstractContainerComponent<TreeNode> {

	public TreeView(ComponentHandler handler) {
		super(handler);
	}

	public enum NodeType {
		SPLITTER, DEFAULT, SUB_HEADER
	}
}