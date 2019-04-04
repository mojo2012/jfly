package io.spotnext.jfly.ui.navigation;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractContainerComponent;

public class TreeView extends AbstractContainerComponent<TreeNode> {

	private boolean allowMultiExpand = true;

	public TreeView(ComponentHandler handler) {
		super(handler);
	}

	public enum NodeType {
		SPLITTER, DEFAULT, SUB_HEADER
	}

	public boolean isAllowMultiExpand() {
		return allowMultiExpand;
	}

	public void setAllowMultiExpand(boolean allowMultiExpand) {
		this.allowMultiExpand = allowMultiExpand;
		updateClientComponent();
	}

}
