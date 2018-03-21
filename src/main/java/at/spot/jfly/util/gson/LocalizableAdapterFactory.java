package at.spot.jfly.util.gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import at.spot.jfly.util.Localizable;

public class LocalizableAdapterFactory implements TypeAdapterFactory {

	@Override
	public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
		final Class<T> rawType = (Class<T>) type.getRawType();

		if (Arrays.stream(rawType.getInterfaces()).filter((i) -> i.equals(Localizable.class)).findAny().isPresent()) {
			return new LocalizableEnumTypeAdapter<T>();
		}

		return null;
	}

	public class LocalizableEnumTypeAdapter<T> extends TypeAdapter<T> {

		@Override
		public void write(final JsonWriter out, final T value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}

			final Localizable<Object> object = (Localizable<Object>) value;

			out.beginObject();
			
			for (Map.Entry<Locale, Object> e : object.getEntries()) {
				out.name(e.getKey().toString());
				out.value(e.toString());
			}
				
			out.endObject();
		}

		@Override
		public T read(final JsonReader in) throws IOException {
			return null;
		}
	}


}