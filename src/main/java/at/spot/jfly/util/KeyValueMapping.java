package at.spot.jfly.util;

import java.util.HashMap;
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
}
