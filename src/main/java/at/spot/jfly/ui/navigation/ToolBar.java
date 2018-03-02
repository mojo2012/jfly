package at.spot.jfly.ui.navigation;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.layout.Alignment;
import at.spot.jfly.style.ComponentType;
import at.spot.jfly.style.NavbarStyle;
import at.spot.jfly.ui.base.AbstractContainerComponent;
import at.spot.jfly.ui.base.AbstractTextComponent;

public class ToolBar extends AbstractContainerComponent<AbstractTextComponent> {

	private transient Alignment placement = Alignment.Top;
	private AbstractTextComponent header = null;

	public ToolBar(final ComponentHandler handler, final NavbarStyle style) {
		super(handler);
		componentType(ComponentType.NavBar);
		addStyleClasses(style);
	}

	public <C extends AbstractTextComponent> C header() {
		return (C) this.header;
	}

	public <C extends AbstractTextComponent> ToolBar header(final C header) {
		this.header = header;
		this.header.addStyleClasses(NavbarStyle.NavBarHeaderBrand);
		return this;
	}

	public Alignment placement() {
		return this.placement;
	}

	public ToolBar placement(final Alignment placement) {
		this.placement = placement;
		return this;
	}

}