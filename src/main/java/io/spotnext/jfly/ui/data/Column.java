package io.spotnext.jfly.ui.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.spotnext.jfly.attributes.Alignment;
import io.spotnext.jfly.util.Localizable;

public class Column {
	private String value;
	private Localizable<String> text;
	@JsonProperty(value = "align")
	private Alignment alignment;
	private boolean sortable = true;
	private int width;

	/**
	 * @param value
	 *            the id of the column, must match the {@link DataTableRow}
	 *            object's property for the given column.
	 * @param text
	 *            to display in the header bar
	 */
	public Column(String value, String text) {
		this(value, Localizable.of(text));
	}

	/**
	 * @param value
	 *            the id of the column, must match the {@link DataTableRow}
	 *            object's property for the given column.
	 * @param text
	 *            to display in the header bar
	 */
	public Column(String value, Localizable<String> text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public Localizable<String> getText() {
		return text;
	}

	public void setText(Localizable<String> text) {
		this.text = text;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
