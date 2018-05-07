package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Alignment;
import at.spot.jfly.ui.base.AbstractActionComponent;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.AbstractContainerComponent;
import at.spot.jfly.ui.base.AbstractLabelledComponent;

public class ToolBar extends AbstractContainerComponent<AbstractComponent> {

	private static final String ACTION_ITEM_TAG = "v-toolbar-side-icon";

	private transient Alignment placement = Alignment.Top;
	private AbstractLabelledComponent header = null;
	private boolean isSlim = false;

	private AbstractActionComponent leftActionItem;
	private AbstractActionComponent rightActionItem;

	public ToolBar(final ComponentHandler handler) {
		super(handler);
	}

	public AbstractLabelledComponent getHeader() {
		return this.header;
	}

	public void setHeader(final AbstractLabelledComponent header) {
		this.header = header;
	}

	public Alignment getPlacement() {
		return this.placement;
	}

	public void setPlacement(final Alignment placement) {
		this.placement = placement;
	}

	public void setLeftActionItem(AbstractActionComponent action) {
		this.leftActionItem = action;
		this.leftActionItem.setFlat(true);
	}

	public void setRightActionItem(AbstractActionComponent action) {
		this.rightActionItem = action;
		this.rightActionItem.setFlat(true);
	}

	public AbstractActionComponent getLeftActionItem() {
		return leftActionItem;
	}

	public AbstractActionComponent getRightActionItem() {
		return rightActionItem;
	}

	public boolean isSlim() {
		return isSlim;
	}

	public void setSlim(boolean isSlim) {
		this.isSlim = isSlim;
	}
}
