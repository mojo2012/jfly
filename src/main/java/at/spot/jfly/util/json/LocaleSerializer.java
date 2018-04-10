package at.spot.jfly.util.json;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class LocaleSerializer extends StdSerializer<Locale> {

	private static final long serialVersionUID = 1L;

	public LocaleSerializer() {
		this(null);
	}

	public LocaleSerializer(Class<Locale> type) {
		super(type);
	}

	@Override
	public void serialize(Locale value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeStartObject();
		jgen.writeStringField("code", value.toString());
		jgen.writeStringField("language", value.getLanguage());
		jgen.writeStringField("country", value.getCountry());
		jgen.writeEndObject();
	}
}