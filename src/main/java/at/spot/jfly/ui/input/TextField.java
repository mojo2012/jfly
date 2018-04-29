package at.spot.jfly.ui.input;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.attributes.Attributes.TextFieldType;
import at.spot.jfly.event.EventHandler;
import at.spot.jfly.event.Events.JsEvent;
import at.spot.jfly.ui.base.AbstractTextComponent;
import at.spot.jfly.util.Localizable;

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

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	@JsonIgnore
	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	@JsonIgnore
	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public boolean isCounterVisible() {
		return counterVisible;
	}

	public void setCounterVisible(boolean counterVisible) {
		this.counterVisible = counterVisible;
	}

	/*
	 * EVENT HANDLERS
	 */

	public TextFieldType getType() {
		return type;
	}

	public void setType(TextFieldType type) {
		this.type = type;
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
