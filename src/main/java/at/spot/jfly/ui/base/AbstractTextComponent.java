package at.spot.jfly.ui.base;

import at.spot.jfly.ComponentHandler;

public abstract class AbstractTextComponent extends AbstractComponent {
	private String text;

	public AbstractTextComponent(final ComponentHandler handler, final String text) {
		super(handler);
		text(text);
	}

	public <C extends AbstractTextComponent> C text(final String text) {
		this.text = text;
		updateClientComponent();
		return (C) this;
	}

	public String text() {
		return this.text;
	}
}
