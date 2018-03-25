package at.spot.jfly.util;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import at.spot.jfly.util.gson.DynamicEnumSerializer;
import at.spot.jfly.util.gson.LocaleSerializer;
import at.spot.jfly.util.gson.LocalizableStringSerializer;

public class JsonUtil {
	// private static final Gson gson;
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		// GsonBuilder builder = new
		// GsonFireBuilder().enableExposeMethodResult().createGsonBuilder();
		// builder.registerTypeAdapterFactory(new ModifierEnumAdapterFactory());
		// builder.registerTypeAdapterFactory(new LocalizableAdapterFactory());

		// mapper.enableDefaultTyping();

		Localizable<String> localizable = new Localizable<String>() {
		};

		SimpleModule module = new SimpleModule();
		module.addSerializer(Locale.class, new LocaleSerializer());
		module.addSerializer(localizable.getClass(), new LocalizableStringSerializer());
		module.addSerializer(Enum.class, new DynamicEnumSerializer());
		mapper.registerModule(module);
		// mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);

		// gson = builder.create();
	}

	public static String toJson(final Object object) {
		// return gson.toJson(object);
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T> T fromJson(final String json, final Class<T> classOfT) {
		try {
			return mapper.readValue(json, classOfT);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		// return gson.fromJson(json, classOfT);
	}
}
