package at.spot.jfly.util;

import java.util.Collections;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Localizable<T> {
	@JsonIgnore
	private final KeyValueMapping<Locale, T> values = new KeyValueMapping<>();

	public static <T> Localizable<T> of(T value) {
		return new Localizable<>(Locale.getDefault(), value);
	}

	public static <T> Localizable<T> of(Locale locale, T value) {
		return new Localizable<>(locale, value);
	}

	private Localizable(Locale locale, T value) {
		this.values.put(locale, value);
	}

	/**
	 * Returns the value associated with the default locale
	 * ({@link Locale#getDefault()}).
	 */
	public T get() {
		return values.get(Locale.getDefault());
	}

	public T get(Locale locale) {
		return values.get(locale);
	}

	/**
	 * Returns the toString of the value for the default locale
	 * ({@link Locale#getDefault()}).
	 */
	@Override
	public String toString() {
		return values.transformValue(Locale.getDefault(), v -> v.toString());
	}

	/**
	 * Returns toString of the localized value.
	 * 
	 * @return null if there is no localized.
	 */
	public String toString(Locale locale) {
		return values.transformValue(locale, v -> v.toString());
	}

	public Set<Entry<Locale, T>> getEntries() {
		return Collections.unmodifiableSet(values.entrySet());
	}
}
