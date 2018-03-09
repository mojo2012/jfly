package at.spot.jfly.ui.input;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.JsEvent;
import at.spot.jfly.ui.base.AbstractTextComponent;

public class TextField extends AbstractTextComponent {

	private String placeholder;
	private String label;
	private boolean multiLine = false;

	public TextField(ComponentHandler handler) {
		this(handler, null);
	}

	public TextField(ComponentHandler handler, String text) {
		super(handler, text);
	}

	public boolean isMultiLine() {
		return multiLine;
	}

	public void setMultiLine(boolean isMultiLine) {
		this.multiLine = isMultiLine;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

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
