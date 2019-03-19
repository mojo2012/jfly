package io.spotnext.jfly.ui.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.util.Container;
import io.spotnext.jfly.util.Localizable;

public class DataTable<I extends DataTableRow> extends AbstractComponent implements Container<I> {

	private final List<Column> columns = new ArrayList<>();
	private final List<I> children = new ArrayList<>();

	private boolean allowSelect = false;
	private boolean showActions = true;
	private boolean showColumnHeader = true;
	private Localizable<String> noDataText;
	private Localizable<String> noResultsText;
	private Localizable<String> rowsPerPageText;
	private List<Integer> rowsPerPageItems = List.of(5, 10, 25, 50, 100, 200);
	private String filterText = "";

	public DataTable(ComponentHandler handler) {
		super(handler);
	}

	@Override
	public void addChildren(I... children) {
		this.children.addAll(Arrays.asList(children));
		updateClientComponent();
	}

	@Override
	public void removeChildren(I... children) {
		this.children.removeAll(Arrays.asList(children));
		updateClientComponent();
	}

	@Override
	public List<I> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	public void addColumn(final Column column) {
		columns.add(column);
		updateClientComponent();
	}

	public void removeColumn(final Column column) {
		columns.remove(column);
		updateClientComponent();
	}

	public List<Column> getColumns() {
		return Collections.unmodifiableList(columns);
	}

	public boolean isAllowSelect() {
		return allowSelect;
	}

	public void setAllowSelect(boolean allowSelect) {
		this.allowSelect = allowSelect;
		updateClientComponent();
	}

	public List<I> getSelected() {
		return this.children.stream().filter(I::isSelected).collect(Collectors.toList());
	}

	public boolean isShowActions() {
		return showActions;
	}

	public void setShowActions(boolean showActions) {
		this.showActions = showActions;
		updateClientComponent();
	}

	public boolean isShowColumnHeader() {
		return showColumnHeader;
	}

	public void setShowColumnHeader(boolean showColumnHeader) {
		this.showColumnHeader = showColumnHeader;
		updateClientComponent();
	}

	public Localizable<String> getNoDataText() {
		return noDataText;
	}

	public void setNoDataText(Localizable<String> noDataText) {
		this.noDataText = noDataText;
		updateClientComponent();
	}

	public Localizable<String> getNoResultsText() {
		return noResultsText;
	}

	public void setNoResultsText(Localizable<String> noResultsText) {
		this.noResultsText = noResultsText;
		updateClientComponent();
	}

	public Localizable<String> getRowsPerPageText() {
		return rowsPerPageText;
	}

	public void setRowsPerPageText(Localizable<String> rowsPerPageText) {
		this.rowsPerPageText = rowsPerPageText;
		updateClientComponent();
	}

	public String getFilterText() {
		return filterText;
	}

	public void setFilterText(String filterText) {
		this.filterText = filterText;
		updateClientComponent();
	}

	public List<Integer> getRowsPerPageItems() {
		return rowsPerPageItems;
	}

	public void setRowsPerPageItems(List<Integer> rowsPerPageItems) {
		this.rowsPerPageItems = rowsPerPageItems;
		updateClientComponent();
	}


}
