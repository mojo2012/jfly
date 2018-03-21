package at.spot.jfly.ui.action;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.style.NavigationTarget;
import at.spot.jfly.ui.base.AbstractActionComponent;
import at.spot.jfly.util.Localizable;

/**
 * Implements a generic link.
 */
public class LinkAction extends AbstractActionComponent {
	private static final String EMPTY_LOCATION = "javascript:void(0)";

	private String location;
	private NavigationTarget navigationTarget = NavigationTarget.Blank;

	public LinkAction(final ComponentHandler handler, final Localizable<String> text) {
		super(handler, text);
		setLocation(EMPTY_LOCATION);
	}

	public LinkAction(final ComponentHandler handler, final Localizable<String> text, String url,
			NavigationTarget navigationTarget) {

		this(handler, text);
		this.location = url;
		this.navigationTarget = navigationTarget;
	}

	public void setLocation(final String location) {
		this.location = location;
		updateClientComponent();
	}

	public String getLocation() {
		return this.location;
	}

	public NavigationTarget getNavigationTarget() {
		return navigationTarget;
	}

	public void setNavigationTarget(NavigationTarget navigationTarget) {
		this.navigationTarget = navigationTarget;
		updateClientComponent();
	}
}
