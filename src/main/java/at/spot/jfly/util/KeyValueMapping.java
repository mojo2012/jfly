package at.spot.jfly.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KeyValueMapping<K, V> extends ConcurrentHashMap<K, V> {
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

	@Override
	public V remove(Object key) {
		if (key != null) {
			return super.remove(key);
		} else {
			return null;
		}
	}

	@Override
	public V put(K key, V value) {
		if (value != null) {
			return super.put(key, value);
		} else {
			return super.remove(key);
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		if (map != null) {
			super.putAll(map);
		}
	}

	public void putAll(Collection<V> collection, Function<V, K> keyMapper) {
		if (collection != null) {
			super.putAll(collection.stream().collect(Collectors.toMap(keyMapper, Function.identity())));
		}
	}

	public <T> void putAll(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper) {
		if (collection != null) {
			super.putAll(collection.stream().collect(Collectors.toMap(keyMapper, valueMapper)));
		}

	}
}
