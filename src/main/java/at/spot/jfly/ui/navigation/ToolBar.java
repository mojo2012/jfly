package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Alignment;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.AbstractContainerComponent;
import at.spot.jfly.ui.base.AbstractLabelledComponent;

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
		this.leftActionItem.setFlat(true);
	}

	public void setRightActionItem(AbstractComponent action) {
		this.rightActionItem = action;
		this.rightActionItem.setFlat(true);
	}

	public Button getLeftActionItem() {
		return leftActionItem;
	}

	public Button getRightActionItem() {
		return rightActionItem;
	}

	public boolean isSlim() {
		return isSlim;
	}

	public void setSlim(boolean isSlim) {
		this.isSlim = isSlim;
	}
}
