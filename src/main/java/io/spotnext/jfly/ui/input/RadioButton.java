package io.spotnext.jfly.ui.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.ui.data.RadioButtonEntry;

public class RadioButton extends AbstractComponent {

	private final List<RadioButtonEntry> entries = new ArrayList<>();

	private RadioButtonEntry selected;

	private boolean mandatory;

	public RadioButton(ComponentHandler handler) {
		super(handler);
	}

	public List<RadioButtonEntry> getEntries() {
		return Collections.unmodifiableList(this.entries);
	}

	public void removeEntry(String id) {
		Optional<RadioButtonEntry> entry = this.entries.stream().filter(e -> StringUtils.equals(id, e.getId()))
				.findFirst();
		if (entry.isPresent()) {
			this.entries.remove(entry.get());
		}
	}

	public void clear() {
		this.entries.clear();
	}

	public void addEntry(String id, String label, String value) {
		RadioButtonEntry entry = new RadioButtonEntry();
		entry.setId(id);
		entry.setLabel(label);
		entry.setValue(value);
		this.entries.add(entry);
	}

	public RadioButtonEntry getSelected() {
		return selected;
	}

	public void setSelected(RadioButtonEntry selected) {
		this.selected = selected;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public void select(String id) {
		Optional<RadioButtonEntry> entry = entries.stream().filter(e -> StringUtils.equals(id, e.getId())).findFirst();
		if (entry.isPresent()) {
			setSelected(entry.get());
		}
	}

}
