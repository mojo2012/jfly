package io.spotnext.jfly.ui.base;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.event.EventHandler;
import io.spotnext.jfly.event.Events.JsEvent;
import io.spotnext.jfly.util.Localizable;

public abstract class AbstractLabelledComponent extends AbstractComponent {
	private Localizable<String> text;
	private boolean isFlat = false;

	public boolean isFlat() {
		return isFlat;
	}

	public void setFlat(boolean isFlat) {
		this.isFlat = isFlat;
	}

	public AbstractLabelledComponent(final ComponentHandler handler, final Localizable<String> text) {
		this(handler);
		setText(text);
	}

	public AbstractLabelledComponent(final ComponentHandler handler) {
		super(handler);
	}

	public Localizable<String> getText() {
		return this.text;
	}

	public void setText(Localizable<String> text) {
		this.text = text;
		updateClientComponent();
	}

	public void setText(String text) {
		this.text = Localizable.of(text);
		updateClientComponent();
	}

	public void onHover(final EventHandler handler) {
		onEvent(JsEvent.Hover, handler);
	}
}
