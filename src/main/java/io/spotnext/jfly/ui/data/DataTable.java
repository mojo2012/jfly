package io.spotnext.jfly.ui.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.attributes.TriStateBoolean;
import io.spotnext.jfly.event.DomEvent;
import io.spotnext.jfly.event.EventHandler;
import io.spotnext.jfly.event.Events.DataTableEvents;
import io.spotnext.jfly.event.Events.JsEvent;
import io.spotnext.jfly.ui.base.AbstractComponent;
import io.spotnext.jfly.util.Container;
import io.spotnext.jfly.util.Localizable;
import io.spotnext.support.util.Comparators;

public class DataTable<I extends DataTableRow> extends AbstractComponent implements Container<I> {

	private final List<Column> columns = new ArrayList<>();
	private final List<I> children = new ArrayList<>();

	private boolean allowSelect = false;
	private boolean allowSelectAll = true;
	private boolean showActions = true;
	private boolean showColumnHeader = true;
	private Localizable<String> noDataText = Localizable.of("Empty");
	private Localizable<String> noResultsText = Localizable.of("No matching records found");
	private Localizable<String> rowsPerPageText = Localizable.of("Rows per page");
	private List<Integer> rowsPerPageItems = List.of(5, 10, 25, 50, 100, 200);
	private String filterText = "";
	private final Pagination pagination;

	public DataTable(ComponentHandler handler) {
		super(handler);

		// after properties of the pagination change, we need to update the
		// client component state!
		this.pagination = new Pagination(this::afterPaginationChanged);
		this.pagination.setRowsPerPage(this.rowsPerPageItems.get(0));

		onSelect(this::handleOnSelect);
		onSelectAll(this::handleOnSelectAll);
		onSort(this::handleOnSort);
	}

	private void afterPaginationChanged() {
		updateClientComponent();
	}

	public void onSelect(EventHandler handler) {
		onEvent(JsEvent.Select, handler);
	}

	public void onSelectAll(EventHandler handler) {
		onEvent(DataTableEvents.SelectAll, handler);
	}

	public void onSort(EventHandler handler) {
		onEvent(DataTableEvents.Sort, handler);
	}

	private void handleOnSelect(DomEvent event) {
		final String childId = (String) event.getData().get("childId");
		final Optional<I> selectedChild = children.stream().filter(c -> c.getId().equals(childId)).findFirst();

		if (selectedChild.isPresent()) {
			selectedChild.get().setSelected(!selectedChild.get().isSelected());
			updateClientComponent();
		}
	}

	private void handleOnSelectAll(DomEvent event) {
		boolean select = !TriStateBoolean.True.equals(getAllSelected());

		getStreamOfChildrenOnCurrentPage().forEach(c -> c.setSelected(select));
		updateClientComponent();
	}

	private void handleOnSort(DomEvent event) {
		String sortBy = (String) event.getData().get("sortBy");

		if (StringUtils.isNotBlank(sortBy)) {
			pagination.setDescending(!pagination.isDescending());
			pagination.setSortBy(sortBy);
			this.children.sort(Comparators.propertyBasedComparator(sortBy, !pagination.isDescending()));
			updateClientComponent();
		}
	}

	@Override
	public void addChildren(I... children) {
		this.children.addAll(Arrays.asList(children));
		this.pagination.setTotalItems(this.children.size());
		updateClientComponent();
	}

	@Override
	public void removeChildren(I... children) {
		this.children.removeAll(Arrays.asList(children));
		this.pagination.setTotalItems(this.children.size());
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

	public boolean isAllowSelectAll() {
		return allowSelectAll;
	}

	public void setAllowSelectAll(boolean allowSelectAll) {
		this.allowSelectAll = allowSelectAll;
		updateClientComponent();
	}

	public List<I> getSelected() {
		return this.children.stream().filter(I::isSelected).collect(Collectors.toList());
	}

	public void setSelected(List<I> children) {
		this.children.stream().filter(c -> children.contains(c)).forEach(c -> c.setSelected(true));
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

	public Pagination getPagination() {
		return pagination;
	}

	public void removeAllChildren() {
		this.children.clear();
	}

	/**
	 * Check if all children on the current page are selected
	 */
	public TriStateBoolean getAllSelected() {
		long selected = getStreamOfChildrenOnCurrentPage().filter(DataTableRow::isSelected).count();

		if (selected > 0) {
			if (selected != pagination.getRowsPerPage()) {
				return TriStateBoolean.Intermediate;
			} else {
				return TriStateBoolean.True;
			}
		}

		return TriStateBoolean.False;
	}

	public List<I> getChildrenOnCurrentPage() {
		return getStreamOfChildrenOnCurrentPage().collect(Collectors.toList());
	}

	private Stream<I> getStreamOfChildrenOnCurrentPage() {
		int skip = (pagination.getPage() - 1) * pagination.getRowsPerPage();
		return this.children.stream().skip(skip).limit(pagination.getRowsPerPage());
	}
}
