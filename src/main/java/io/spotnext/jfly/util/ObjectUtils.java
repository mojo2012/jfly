package io.spotnext.jfly.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * Populates an object's field with the values from the given properties.
	 * Unknown properties and type exceptions will be ignored.
	 */
	public static void populate(Object object, Map<String, Object> properties) {

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			Class<?> klass = object.getClass();
			final String propertyName = entry.getKey();
			final Object propertyValue = entry.getValue();

			try {
				while (klass != null) {
					boolean changedAccessLevel = false;

					try {
						Optional<Method> setter = getSetter(klass, propertyName, propertyValue.getClass());

						if (setter.isPresent()) {
							Method setterMethod = setter.get();
							setAccessible(setterMethod, true);

							setterMethod.invoke(object, propertyValue);
						} else {
							Field field = klass.getDeclaredField(propertyName);

							if (!field.isAccessible()) {
								field.setAccessible(true);
								changedAccessLevel = true;
							}

							if (getNonPrimitiveType(propertyValue.getClass())
									.equals(getNonPrimitiveType(field.getType()))) {
								field.set(object, propertyValue);
							} else if (propertyValue instanceof Map) {
								populate(field.get(object), (Map) propertyValue);
							}

							if (changedAccessLevel) {
								field.setAccessible(false);
							}
						}

						klass = null;
					} catch (NoSuchFieldException | NoSuchMethodException e) {
						klass = klass.getSuperclass();
						continue;
					}
				}
			} catch (SecurityException | IllegalArgumentException | IllegalAccessException
					| InvocationTargetException e) {
				LOG.warn("Could not populate object of type " + object.getClass().getName());
			}

		}
	}

	private static void setAccessible(Member member, boolean value) {
		if (member instanceof Field) {
			((Field) member).setAccessible(true);
		} else if (member instanceof Method) {
			((Method) member).setAccessible(true);
		}
	}

	private static Optional<Method> getSetter(Class<?> klass, String propertyName, Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {

		try {
			return Optional.of(klass.getDeclaredMethod("set" + StringUtils.capitalize(propertyName), parameterTypes));
		} catch (NoSuchMethodException e) {

		}

		return Optional.empty();
	}

	private static Class<?> getNonPrimitiveType(Class<?> type) {
		if (type.isPrimitive()) {
			return ClassUtils.primitiveToWrapper(type);
		}

		return type;
	}
}
