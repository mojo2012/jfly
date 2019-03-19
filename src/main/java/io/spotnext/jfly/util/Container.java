package io.spotnext.jfly.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface Container<T> {

	/**
	 * Add the given children into the container.
	 * 
	 * @param children
	 */
	void addChildren(T... children);

	/**
	 * Remove the given children from the container
	 * 
	 * @param children
	 */
	void removeChildren(T... children);

	/**
	 * Returns the container's children
	 * 
	 * @return can never be null
	 */
	List<T> getChildren();

	/**
	 * @return true if the container has some children.
	 */
	@JsonProperty(value = "hasChildren")
	default boolean hasChildren() {
		return getChildren().size() > 0;
	}

}
