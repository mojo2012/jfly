package at.spot.jfly.ui.base;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.util.Localizable;

public abstract class AbstractTextComponent extends AbstractComponent {
	private Localizable<String> text;
	private boolean isFlat = false;

	public boolean isFlat() {
		return isFlat;
	}

	public void setFlat(boolean isFlat) {
		this.isFlat = isFlat;
	}

	public AbstractTextComponent(final ComponentHandler handler, final Localizable<String> text) {
		super(handler);
		text(text);
	}

	public <C extends AbstractTextComponent> C text(final Localizable<String> text) {
		this.text = text;
		updateClientComponent();
		return (C) this;
	}

	public Localizable<String> getText() {
		return this.text;
	}

	public void setText(Localizable<String> text) {
		this.text = text;
	}

	public <C extends AbstractActionComponent> C onHover(final EventHandler handler) {
		onEvent(JsEvent.hover, handler);

		return (C) this;
	}
}
