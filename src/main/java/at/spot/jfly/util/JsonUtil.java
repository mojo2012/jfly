package at.spot.jfly.util;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

import at.spot.jfly.event.Events.EventEnum;
import at.spot.jfly.event.Events.EventType;
import at.spot.jfly.util.json.DynamicEnumSerializer;
import at.spot.jfly.util.json.LocaleSerializer;
import at.spot.jfly.util.json.LocalizableStringSerializer;

public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		Localizable<String> localizable = new Localizable<String>() {
		};

		SimpleModule module = new SimpleModule();
		module.addSerializer(Locale.class, new LocaleSerializer());
		module.addSerializer(localizable.getClass(), new LocalizableStringSerializer());
		module.addSerializer(Enum.class, new DynamicEnumSerializer());

		SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
		resolver.addMapping(EventType.class, EventEnum.class);
		module.setAbstractTypes(resolver);

		mapper.registerModule(module);
	}

	/**
	 * Converts the given object to a JSON string.
	 * 
	 * @throws IllegalStateException
	 */
	public static String toJson(final Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * The JSON string will be parsed and then the object will be updated with the
	 * values in the JSON object.
	 * 
	 * @throws IllegalStateException
	 */
	public static void update(Object object, String json) {
		try {
			mapper.readerForUpdating(object).readValue(json);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Creates a new instance of the given type from the given JSON string.
	 * 
	 * @throws IllegalStateException
	 */
	public static <T> T fromJson(final String json, final Class<T> classOfT) {
		try {
			return mapper.readValue(json, classOfT);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
