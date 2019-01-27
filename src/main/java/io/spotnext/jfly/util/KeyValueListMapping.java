package io.spotnext.jfly.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyValueListMapping<K, V> extends KeyValueMapping<K, List<V>> {
	private static final long serialVersionUID = 1L;

	/**
	 * Adds the value to the list of values associated with the key. If there are no
	 * values yet, a new list is created.
	 */
	public List<V> putOrAdd(K key, V value) {
		List<V> values = super.get(key);

		if (values == null) {
			values = new ArrayList<>();
			put(key, values);
		}

		values.add(value);

		return values;
	}

	/**
	 * Removes the value from the list of values associated with the key.
	 */
	@Override
	public boolean remove(Object key, Object value) {
		List<V> values = super.get(key);

		if (values != null) {
			return values.remove(value);
		}

		return false;
	}

	/**
	 * Returns the list of values for the given key. If the key is not found, an
	 * empty list is returned.
	 */
	@Override
	public List<V> get(Object key) {
		List<V> values = super.get(key);

		return values != null ? values : Collections.emptyList();
	}

}
