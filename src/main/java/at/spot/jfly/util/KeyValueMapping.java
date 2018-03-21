package at.spot.jfly.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class KeyValueMapping<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Executes the given consumer if there is a value for the given key. If there
	 * is no value, the call will be ignored.
	 * 
	 * @param consumer
	 *            executed with the found value
	 */
	public void apply(K key, Consumer<V> consumer) {
		V value = super.get(key);

		if (value != null && consumer != null) {
			consumer.accept(value);
		}
	}

	/**
	 * Executes the given consumer if there is a value for the given key.
	 * 
	 * @param consumer
	 *            executed with the found value
	 * @param nullConsumer
	 *            will be called when there is no value found for the given key.
	 */
	public void apply(K key, Consumer<V> consumer, Consumer<Void> nullConsumer) {
		V value = super.get(key);

		if (value != null && consumer != null) {
			consumer.accept(value);
		} else if (nullConsumer != null) {
			nullConsumer.accept(null);
		}
	}

	/**
	 * Returns the transformed value for the given key.
	 * 
	 * @param key
	 * @param transformer
	 * @param nullSupplier
	 *            called when there is no value found for the given key
	 * @return null if no value is found and the nullSupplier is null
	 */
	public <R> R transformValue(K key, Function<V, R> transformer, Supplier<R> nullSupplier) {
		V value = super.get(key);

		if (value != null && transformer != null) {
			return transformer.apply(value);
		} else if (nullSupplier != null) {
			return nullSupplier.get();
		}

		return null;
	}

	/**
	 * Returns the transformed value for the given key.
	 * 
	 * @return null if no value is found.
	 */
	public <R> R transformValue(K key, Function<V, R> transformer) {
		return transformValue(key, transformer, null);
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
