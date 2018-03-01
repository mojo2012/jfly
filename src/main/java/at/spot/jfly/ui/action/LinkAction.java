package at.spot.jfly.ui.action;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.style.NavigationTarget;
import at.spot.jfly.ui.base.AbstractActionComponent;

/**
 * Implements a generic link.
 */
public class LinkAction extends AbstractActionComponent {
	private String location;
	private NavigationTarget navigationTarget = NavigationTarget.Blank;

	public LinkAction(final ComponentHandler handler, final String text) {
		super(handler, text);
	}

	public LinkAction(final ComponentHandler handler, final String text, String url,
			NavigationTarget navigationTarget) {

		this(handler, text);
		this.location = url;
		this.navigationTarget = navigationTarget;
	}

	public LinkAction location(final String location) {
		this.location = location;
		updateClientComponent();
		return this;
	}

	public String location() {
		return this.location;
	}

	public NavigationTarget navigationTarget() {
		return navigationTarget;
	}

	public LinkAction navigationTarget(NavigationTarget navigationTarget) {
		this.navigationTarget = navigationTarget;
		updateClientComponent();
		return this;
	}
}
