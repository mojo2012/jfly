package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractContainerComponent;
import at.spot.jfly.ui.display.Icon;

public class TreeNode extends AbstractContainerComponent<TreeNode> {

	private String title;
	private String subTitle;
	private String badge;
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

	public TreeNode(ComponentHandler handler, String title) {
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
			updateClient(ClientDrawFunction.REPLACE, this.icon.getUuid(), icon);
		} else {
			updateClient(ClientDrawFunction.ADD, icon);
		}

		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		updateClientComponent();
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
		updateClientComponent();
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
		updateClientComponent();
	}

	public boolean isSplitter() {
		return NodeType.SPLITTER.equals(nodeType);
	}

	public boolean isSubHeader() {
		return NodeType.SUB_HEADER.equals(nodeType);
	}

	public enum NodeType {
		SPLITTER, DEFAULT, SUB_HEADER
	}
}
