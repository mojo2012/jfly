package at.spot.jfly.ui.base;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;

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

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public <C extends AbstractActionComponent> C onHover(final EventHandler handler) {
		onEvent(JsEvent.hover, handler);

		return (C) this;
	}
}
