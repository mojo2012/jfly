package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Alignment;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.ui.base.AbstractContainerComponent;
import at.spot.jfly.ui.base.AbstractTextComponent;
import at.spot.jfly.ui.generic.GenericContainer;

public class ToolBar extends AbstractContainerComponent<AbstractTextComponent> {

	private static final String ACTION_ITEM_TAG = "v-toolbar-side-icon";

	private transient Alignment placement = Alignment.Top;
	private AbstractTextComponent header = null;

	private GenericContainer leftActionItem;
	private GenericContainer rightActionItem;

	public ToolBar(final ComponentHandler handler) {
		super(handler);
	}

	public AbstractTextComponent getHeader() {
		return this.header;
	}

	public void setHeader(final AbstractTextComponent header) {
		this.header = header;
	}

	public Alignment getPlacement() {
		return this.placement;
	}

	public void setPlacement(final Alignment placement) {
		this.placement = placement;
	}

	public void setLeftActionItem(EventHandler onClickHandler) {
		this.leftActionItem = new GenericContainer(getHandler(), ACTION_ITEM_TAG);
		this.leftActionItem.onClick(onClickHandler);
	}

	public void setRightActionItem(EventHandler onClickHandler) {
		this.rightActionItem = new GenericContainer(getHandler(), ACTION_ITEM_TAG);
		this.rightActionItem.onClick(onClickHandler);
	}

	public GenericContainer getLeftActionItem() {
		return leftActionItem;
	}

	public GenericContainer getRightActionItem() {
		return rightActionItem;
	}
}
