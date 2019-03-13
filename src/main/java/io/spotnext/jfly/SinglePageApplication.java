package io.spotnext.jfly;

public abstract class SinglePageApplication extends ViewHandler {

	public static final String VIEW_ID = "0";

	@Override
	public String getViewUid() {
		// always return the same view id, so that there can only be one
		// instance within the same session
		return VIEW_ID;
	}

}
