package io.spotnext.jfly.ui.base;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.event.EventHandler;
import io.spotnext.jfly.event.Events.JsEvent;

public abstract class AbstractTextComponent extends AbstractComponent {
	protected String text;
	protected boolean isFlat = false;

	public boolean isFlat() {
		return isFlat;
	}

	public void setFlat(boolean isFlat) {
		this.isFlat = isFlat;
	}

	public AbstractTextComponent(final ComponentHandler handler, String text) {
		this(handler);
		setText(text);
	}

	public AbstractTextComponent(final ComponentHandler handler) {
		super(handler);
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
		updateClientComponent();
	}

	public void onHover(final EventHandler handler) {
		onEvent(JsEvent.Hover, handler);
	}
}
