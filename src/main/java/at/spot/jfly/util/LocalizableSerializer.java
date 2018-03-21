package at.spot.jfly.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocalizableSerializer<T> extends StdSerializer<Localizable<T>> {
	private static final long serialVersionUID = 1L;

	public LocalizableSerializer() {
		this(null);
	}

	public LocalizableSerializer(Class<Localizable<T>> t) {
		super(t);
	}

	@Override
	public void serialize(Localizable<T> value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeObject(value.getEntries());
	}
}