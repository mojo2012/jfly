package io.spotnext.jfly.ui.data;

public class Pagination {
	private transient Runnable afterPropertiesChanged;

	private String sortBy = "";
	private boolean descending = false;
	private Integer page = 1;
	private Integer rowsPerPage = -1; // -1 for All
	private int totalItems = 0;

	public Pagination(Runnable afterPropertiesChanged) {
		this.afterPropertiesChanged = afterPropertiesChanged;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
		afterPropertiesChanged();
	}

	public boolean isDescending() {
		return descending;
	}

	public void setDescending(boolean descending) {
		this.descending = descending;
		afterPropertiesChanged();
	}

	/**
	 * @return the current page, starting with 1
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * Sets the current page, starging with 1.
	 */
	public void setPage(Integer page) {
		this.page = page < 1 ? 1 : page;
		afterPropertiesChanged();
	}

	public Integer getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(Integer rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
		afterPropertiesChanged();
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
		afterPropertiesChanged();
	}

	private void afterPropertiesChanged() {
		this.afterPropertiesChanged.run();
	}
}
