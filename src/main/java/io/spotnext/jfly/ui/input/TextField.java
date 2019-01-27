package io.spotnext.jfly.ui.input;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.Attributes.TextFieldType;
import io.spotnext.jfly.event.DomEvent;
import io.spotnext.jfly.event.EventHandler;
import io.spotnext.jfly.event.Events.JsEvent;
import io.spotnext.jfly.ui.base.AbstractTextComponent;
import io.spotnext.jfly.util.Localizable;

public class TextField extends AbstractTextComponent {

	private Localizable<String> placeholder;
	private Localizable<String> label;
	private boolean multiLine = false;
	private boolean isReadOnly = false;
	private TextFieldType type = TextFieldType.Text;
	private Integer maxLength;
	private Integer minLength;
	private boolean counterVisible = false;
	private boolean isRequired = false;
	private boolean isError = false;

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
		updateClientComponent();
	}

	public Localizable<String> getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(Localizable<String> placeholder) {
		this.placeholder = placeholder;
		updateClientComponent();
	}

	public Localizable<String> getLabel() {
		return label;
	}

	public void setLabel(Localizable<String> label) {
		this.label = label;
		updateClientComponent();
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
		updateClientComponent();
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
		updateClientComponent();
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
		updateClientComponent();
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
		updateClientComponent();
	}

	public boolean isCounterVisible() {
		return counterVisible;
	}

	public void setCounterVisible(boolean counterVisible) {
		this.counterVisible = counterVisible;
		updateClientComponent();
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
		updateClientComponent();
	}

	public TextFieldType getType() {
		return type;
	}

	public void setType(TextFieldType type) {
		this.type = type;
		updateClientComponent();
	}

	/*
	 * EVENT HANDLERS
	 */

	@Override
	public void handleEvent(DomEvent event) {
		// populate changed text from event data
		if (JsEvent.Change.equals(event.getEventType())) {
			String value = (String) event.getData().get("value");
			this.text = value;
		}

		super.handleEvent(event);
	}

	public void onChange(EventHandler e) {
		onEvent(JsEvent.Change, e);
	}

	public void onFocus(EventHandler e) {
		onEvent(JsEvent.Focus, e);
	}

	public void onInput(EventHandler e) {
		onEvent(JsEvent.Input, e);
	}

	public void onKeyUp(EventHandler e) {
		onEvent(JsEvent.KeyUp, e);
	}

	public void onKeyDown(EventHandler e) {
		onEvent(JsEvent.KeyDown, e);
	}

	public void onKeyPress(EventHandler e) {
		onEvent(JsEvent.KeyPress, e);
	}

}
