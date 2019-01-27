package io.spotnext.jfly.util;

import java.lang.reflect.Field;
import java.util.Map;

public class ObjectUtils {

	/**
	 * Populates an object's field with the values from the given properties.
	 * Unknown properties and type exceptions will be ignored.
	 */
	public static void populate(Object object, Map<String, Object> properties) {
		Class<?> klass = object.getClass();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			try {
				while (klass != null) {
					boolean changedAccessLevel = false;

					try {
						Field field = klass.getDeclaredField(entry.getKey());

						if (!field.isAccessible()) {
							field.setAccessible(true);
							changedAccessLevel = true;
						}

						field.set(object, entry.getValue());
						
						if (changedAccessLevel) {
							field.setAccessible(false);
						}

						klass = null;
					} catch (NoSuchFieldException e) {
						klass = klass.getSuperclass();
						continue;
					}
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// ignore unknown property
			}

		}
	}
}
