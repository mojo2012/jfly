package at.spot.jfly.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

public class LocalizationBundle {

	private long lastRefreshed;
	private int refreshAfterSeconds;
	private final String baseName;
	private final KeyValueMapping<Locale, ResourceBundle> bundles = new KeyValueMapping<>();

	/**
	 * @param baseName
	 *            the name of the localized properties files, eg. "messages" for
	 *            "messages_en.properties"
	 */
	public LocalizationBundle(String baseName) {
		this(baseName, 10);
	}

	public LocalizationBundle(String baseName, int refreshAfterSeconds) {
		this.baseName = baseName;
		this.refreshAfterSeconds = refreshAfterSeconds;
		loadBundles();
	}

	protected void loadBundles() {
		// load all available bundles
		for (Locale l : Locale.getAvailableLocales()) {
			try {

				// TODO: make this autoreloadable?
				// but only use the ones which contain messages
				if (StringUtils.isNotBlank(l.getLanguage())) {
					// WARNING: this returns the bundle of the default language in case there is no
					// locale specific bundle found
					// so we also have to compare the locales
					ResourceBundle bundle = ResourceBundle.getBundle(this.baseName, l);
					if (bundle.keySet().size() > 0 && l.getLanguage().equals(bundle.getLocale().getLanguage())) {
						bundles.put(l, bundle);
					}
				}
			} catch (MissingResourceException e) {
				// ignore
			}
		}

		lastRefreshed = System.currentTimeMillis();
	}

	protected void reloadIfNeeded() {
		if (System.currentTimeMillis() - (refreshAfterSeconds * 1000) > lastRefreshed) {
			loadBundles();
		}
	}

	/**
	 * The arguments will be applied to the localized string using
	 * {@link String#format(String, Object...)} syntax.
	 */
	public Localizable<String> forKey(String key, Object... args) {
		return forKey(key, null, args);
	}

	/**
	 * The arguments will be applied to the localized string using
	 * {@link String#format(String, Object...)} syntax.
	 */
	public Localizable<String> forKey(String key, String defaultValue, Object... args) {
		reloadIfNeeded();

		Localizable<String> localizable = new Localizable<>();
		bundles.forEach((locale, bundle) -> localizable.set(locale, getValue(bundle, key, defaultValue, args)));

		return localizable;
	}

	protected String getValue(ResourceBundle bundle, String key, String defaultValue, Object... args) {
		String value = null;

		try {
			value = bundle.getString(key);
			return String.format(value, args);
		} catch (MissingResourceException e) {
			value = defaultValue;
		}

		return value;
	}

	public String getBaseName() {
		return this.baseName;
	}
}
