package at.spot.jfly.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DynamicEnumSerializer extends StdSerializer<at.spot.jfly.util.Enum> {

	private static final long serialVersionUID = 1L;

	public DynamicEnumSerializer() {
		this(null);
	}

	public DynamicEnumSerializer(Class<at.spot.jfly.util.Enum> type) {
		super(type);
	}

	@Override
	public void serialize(at.spot.jfly.util.Enum value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeString(value.getInternalName());
	}
}