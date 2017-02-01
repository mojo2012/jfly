package at.spot.jfly.gson;

import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import at.spot.jfly.style.Style;

public class StyleEnumAdapterFactory implements TypeAdapterFactory {

	@Override
	public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
		final Class<? super T> rawType = type.getRawType();

		if (Arrays.stream(rawType.getInterfaces()).filter((i) -> i.equals(Style.class)).findAny().isPresent()) {
			return new StyleEnumTypeAdapter<T>();
		}

		return null;
	}

	public class StyleEnumTypeAdapter<T> extends TypeAdapter<T> {

		@Override
		public void write(final JsonWriter out, final T value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}

			final Style enumValue = (Style) value;

			out.beginObject();
			out.name("internal");
			out.value(enumValue.internalName());
			out.endObject();
		}

		@Override
		public T read(final JsonReader in) throws IOException {
			return null;
		}
	}

}