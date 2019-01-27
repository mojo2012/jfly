package io.spotnext.jfly.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import io.spotnext.jfly.util.Localizable;

public class LocalizableStringSerializer extends StdSerializer<Localizable> {

	private static final long serialVersionUID = 1L;

	public LocalizableStringSerializer() {
		this(null);
	}

	public LocalizableStringSerializer(Class<Localizable> type) {
		super(type);
	}

	@Override
	public void serialize(Localizable value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeObject(value.getValues());
	}
}