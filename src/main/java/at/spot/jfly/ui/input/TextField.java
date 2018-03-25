package at.spot.jfly.ui.input;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.ui.base.AbstractTextComponent;
import at.spot.jfly.util.Localizable;

public class TextField extends AbstractTextComponent {

	private Localizable<String> placeholder;
	private Localizable<String> label;
	private boolean multiLine = false;
	private boolean isReadOnly = false;

	public TextField(ComponentHandler handler) {
		this(handler, null);
	}

	public TextField(ComponentHandler handler, Localizable<String> text) {
		super(handler, text);
	}

	public boolean isMultiLine() {
		return multiLine;
	}

	public void setMultiLine(boolean isMultiLine) {
		this.multiLine = isMultiLine;
	}

	public Localizable<String> getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(Localizable<String> placeholder) {
		this.placeholder = placeholder;
	}

	public Localizable<String> getLabel() {
		return label;
	}

	public void setLabel(Localizable<String> label) {
		this.label = label;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	/*
	 * EVENT HANDLERS
	 */

	public void onChange(EventHandler e) {
		onEvent(JsEvent.change, e);
	}

	public void onFocus(EventHandler e) {
		onEvent(JsEvent.focus, e);
	}

	public void onInput(EventHandler e) {
		onEvent(JsEvent.input, e);
	}

	public void onKeyUp(EventHandler e) {
		onEvent(JsEvent.keyup, e);
	}

	public void onKeyDown(EventHandler e) {
		onEvent(JsEvent.keydown, e);
	}

	public void onKeyPress(EventHandler e) {
		onEvent(JsEvent.keypress, e);
	}
}
