package io.spotnext.jfly;

import java.util.UUID;

public abstract class PageOrientedApplication extends ViewHandler {

	private String viewId = UUID.randomUUID().toString();

	@Override
	public String getViewUid() {
		return viewId;
	}

}
