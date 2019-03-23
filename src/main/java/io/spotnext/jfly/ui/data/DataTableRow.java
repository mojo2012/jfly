package io.spotnext.jfly.ui.data;

public interface DataTableRow extends Comparable<DataTableRow> {

	String getId();

	boolean isSelected();

	void setSelected(boolean selected);

	@Override
	default int compareTo(DataTableRow o) {
		if (o == null) {
			return -1;
		}

		return getId().compareTo(o.getId());
	}
}
