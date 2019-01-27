package io.spotnext.jfly.ui.navigation;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractContainerComponent;
import io.spotnext.jfly.ui.display.Icon;
import io.spotnext.jfly.ui.navigation.TreeView.NodeType;
import io.spotnext.jfly.util.Localizable;

public class TreeNode extends AbstractContainerComponent<TreeNode> {

	private Localizable<String> title;
	private Localizable<String> subTitle;
	private Localizable<String> badge;
	private Icon icon;
	private boolean isExpanded = false;
	private NodeType nodeType = NodeType.DEFAULT;

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
		updateClientComponent();
	}

	public TreeNode(ComponentHandler handler, Localizable<String> title) {
		super(handler);
		this.title = title;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
		updateClientComponent();
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		if (this.icon != null) {
			updateClient(ComponentManipulationFunction.REPLACE, this.icon.getUuid(), icon);
		} else {
			updateClient(ComponentManipulationFunction.ADD, icon);
		}

		this.icon = icon;
	}

	public Localizable<String> getTitle() {
		return title;
	}

	public void setTitle(Localizable<String> title) {
		this.title = title;
		updateClientComponent();
	}

	public Localizable<String> getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(Localizable<String> subTitle) {
		this.subTitle = subTitle;
		updateClientComponent();
	}

	public Localizable<String> getBadge() {
		return badge;
	}

	public void setBadge(Localizable<String> badge) {
		this.badge = badge;
		updateClientComponent();
	}

	public boolean isSplitter() {
		return NodeType.SPLITTER.equals(nodeType);
	}

	public boolean isSubHeader() {
		return NodeType.SUB_HEADER.equals(nodeType);
	}
}
