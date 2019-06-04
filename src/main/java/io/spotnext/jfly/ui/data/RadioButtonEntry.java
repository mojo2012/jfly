package io.spotnext.jfly.ui.data;

public class RadioButtonEntry implements Comparable<RadioButtonEntry> {

	String id;

	String label;

	String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(RadioButtonEntry o) {
		if (o == null) {
			return -1;
		}

		return getId().compareTo(o.getId());
	}

}
