package io.spotnext.jfly.ui.navigation;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Attributes.HorizontalOrientation;

public class Drawer extends SideBar {

	private ToolBar toolBar;
	private HorizontalOrientation horizontalOrientation;
	private boolean floating = true;

	public Drawer(final ComponentHandler handler, HorizontalOrientation horizontalOrientation) {
		super(handler);
		this.horizontalOrientation = horizontalOrientation;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(ToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public HorizontalOrientation getHorizontalOrientation() {
		return horizontalOrientation;
	}

	public void setHorizontalOrientation(HorizontalOrientation horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}

	public boolean isFloating() {
		return floating;
	}

	public void setFloating(boolean floating) {
		this.floating = floating;
	}

}
