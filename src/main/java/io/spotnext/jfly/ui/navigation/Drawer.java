package io.spotnext.jfly.ui.navigation;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Attributes.HorizontalOrientation;

public class Drawer extends SideBar {

	private ToolBar toolBar;
	private HorizontalOrientation horizontalOrientation;
	private boolean floating = true;
	private int width = 300;

	public Drawer(final ComponentHandler handler, HorizontalOrientation horizontalOrientation) {
		super(handler);
		this.horizontalOrientation = horizontalOrientation;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(ToolBar toolBar) {
		this.toolBar = toolBar;
		updateClientComponent();
	}

	public HorizontalOrientation getHorizontalOrientation() {
		return horizontalOrientation;
	}

	public void setHorizontalOrientation(HorizontalOrientation horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
		updateClientComponent();
	}

	public boolean isFloating() {
		return floating;
	}

	public void setFloating(boolean floating) {
		this.floating = floating;
		updateClientComponent();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		updateClientComponent();
	}

}
