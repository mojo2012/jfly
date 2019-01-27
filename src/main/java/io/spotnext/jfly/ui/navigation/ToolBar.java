package io.spotnext.jfly.ui.navigation;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Alignment;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.ui.base.AbstractContainerComponent;
import io.spotnext.jfly.ui.base.AbstractLabelledComponent;

public class ToolBar extends AbstractContainerComponent<AbstractComponent> {

	private static final String ACTION_ITEM_TAG = "v-toolbar-side-icon";

	private transient Alignment placement = Alignment.Top;
	private AbstractLabelledComponent header = null;
	private boolean isSlim = false;

	private AbstractComponent leftActionItem;
	private AbstractComponent rightActionItem;

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

	public void setLeftActionItem(AbstractComponent action) {
		this.leftActionItem = action;
	}

	public void setRightActionItem(AbstractComponent action) {
		this.rightActionItem = action;
	}

	public AbstractComponent getLeftActionItem() {
		return leftActionItem;
	}

	public AbstractComponent getRightActionItem() {
		return rightActionItem;
	}

	public boolean isSlim() {
		return isSlim;
	}

	public void setSlim(boolean isSlim) {
		this.isSlim = isSlim;
	}
}
