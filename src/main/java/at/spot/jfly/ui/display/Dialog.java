package at.spot.jfly.ui.display;

import java.util.ArrayList;
import java.util.List;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Attributes.Transitions;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.base.AbstractActionComponent;
import at.spot.jfly.ui.base.AbstractComponent;
import at.spot.jfly.ui.base.AbstractContainerComponent;
import at.spot.jfly.util.Localizable;

/**
 * By default a dialog is set to visible=false.
 */
public class Dialog extends AbstractContainerComponent<AbstractComponent> {

	final private List<AbstractActionComponent> actions = new ArrayList<>();

	private boolean isFullScreen = false;
	private boolean isScrollable = true;
	private boolean isOverlay = false;
	private Transitions transition = Transitions.None;
	private Localizable<String> title;
	private Integer maxWidth = null;
	private boolean isPersistent = false;

	public Dialog(final ComponentHandler handler) {
		super(handler);
		setVisible(false);
	}

	public boolean isScrollable() {
		return isScrollable;
	}

	public void setScrollable(boolean isScrollable) {
		this.isScrollable = isScrollable;
	}

	public boolean isFullScreen() {
		return isFullScreen;
	}

	public void setFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	public boolean isOverlay() {
		return isOverlay;
	}

	public void setOverlay(boolean isOverlay) {
		this.isOverlay = isOverlay;
	}

	public Transitions getTransition() {
		return transition;
	}

	public void setTransition(Transitions transition) {
		this.transition = transition;
	}

	public Localizable<String> getTitle() {
		return title;
	}

	public void setTitle(Localizable<String> title) {
		this.title = title;
	}

	public void addAction(AbstractActionComponent action) {
		actions.add(action);
	}

	public void removeAction(Button action) {
		actions.remove(action);
	}

	public List<AbstractActionComponent> getActions() {
		return actions;
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	public boolean isPersistent() {
		return isPersistent;
	}

	public void setPersistent(boolean persistent) {
		this.isPersistent = persistent;
	}

}
