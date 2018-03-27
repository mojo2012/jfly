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
		this(handler);
		setText(text);
	}

	public AbstractTextComponent(final ComponentHandler handler) {
		super(handler);
	}

	public Localizable<String> getText() {
		return this.text;
	}

	public void setText(Localizable<String> text) {
		this.text = text;
		updateClientComponent();
	}

	public void onHover(final EventHandler handler) {
		onEvent(JsEvent.hover, handler);
	}
}
