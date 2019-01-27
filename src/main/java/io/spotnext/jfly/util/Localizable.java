package io.spotnext.jfly.util;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class Localizable<T> {
	private final KeyValueMapping<Locale, T> values = new KeyValueMapping<>();

	private T defaultValue = null;

	/**
	 * Generates a {@link Localizable} that only holds one value - for all locales.
	 * This value overrides all localalized values when using
	 * {@link Localizable#toString()} or {@link Localizable#get()}.
	 */
	public static <T> Localizable<T> of(T value) {
		return new Localizable<>(value);
	}

	public static <T> Localizable<T> of(Locale locale, T value) {
		return new Localizable<>(locale, value);
	}

	private Localizable(T value) {
		this.defaultValue = value;
	}

	private Localizable(Locale locale, T value) {
		this.values.put(locale, value);
	}

	public Localizable() {
	}

	public void set(Locale locale, T value) {
		values.put(locale, value);
	}

	/**
	 * Returns the value associated with the default locale
	 * ({@link Locale#getDefault()}) or the default value, if it is set.
	 */
	public T get() {
		return defaultValue != null ? defaultValue : values.get(Locale.getDefault());
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
		if (defaultValue != null) {
			return defaultValue.toString();
		}

		return toString(Locale.getDefault());
	}

	/**
	 * Returns toString of the localized value.
	 * 
	 * @return null if there is no localized.
	 */
	public String toString(Locale locale) {
		return values.transformValue(locale, v -> v.toString());
	}

	public Map<Locale, T> getValues() {
		return Collections.unmodifiableMap(values);
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}

}
