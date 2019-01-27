package io.spotnext.jfly.ui.action;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.NavigationTarget;
import io.spotnext.jfly.ui.base.AbstractActionComponent;
import io.spotnext.jfly.util.Localizable;

/**
 * Implements a generic link.
 */
public class LinkAction extends AbstractActionComponent {
	private static final String EMPTY_LOCATION = "javascript:void(0)";

	private String location;
	private NavigationTarget navigationTarget;

	public LinkAction(final ComponentHandler handler) {
		this(handler, null);
	}

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
