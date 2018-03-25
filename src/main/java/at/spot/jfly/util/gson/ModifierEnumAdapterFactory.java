package at.spot.jfly.util.gson;

import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import at.spot.jfly.attributes.Attributes.Attribute;

public class ModifierEnumAdapterFactory implements TypeAdapterFactory {

	@Override
	public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
		final Class<? super T> rawType = type.getRawType();

		if (Arrays.stream(rawType.getInterfaces()).filter((i) -> i.equals(Attribute.class)).findAny().isPresent()) {
			return new ModifierEnumTypeAdapter<T>();
		}

		return null;
	}

	public class ModifierEnumTypeAdapter<T> extends TypeAdapter<T> {

		@Override
		public void write(final JsonWriter out, final T value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}

			final Attribute enumValue = (Attribute) value;

			out.beginObject();
			out.name("code");
			out.value(enumValue.toString());
			out.name("name");
			out.value(enumValue.getInternalName());
			out.name("type");
			out.value(enumValue.getClass().getName());
			out.endObject();
		}

		@Override
		public T read(final JsonReader in) throws IOException {
			return null;
		}
	}

}