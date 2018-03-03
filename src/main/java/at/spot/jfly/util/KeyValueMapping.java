package at.spot.jfly.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class KeyValueMapping<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Executes the given consumer if there is a value for the given key.
	 */
	public void applyIfContainsKey(K key, Consumer<V> consumer) {
		V value = super.get(key);

		if (value != null && consumer != null) {
			consumer.accept(value);
		}
	}

	/**
	 * Executes the given consumer if there is no value for the given key.
	 */
	public void applyIfNotContainsKey(K key, Consumer<V> consumer) {
		V value = super.get(key);

		if (value == null && consumer != null) {
			consumer.accept(value);
		}
	}

	public List<V> removeAll(K... keys) {
		List<V> removedValues = null;

		if (keys != null) {
			removedValues = new ArrayList<>();

			for (K key : keys) {
				removedValues.add(remove(key));
			}
		} else {
			removedValues = Collections.emptyList();
		}

		return removedValues;
	}
}
