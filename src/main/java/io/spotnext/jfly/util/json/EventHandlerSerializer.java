package io.spotnext.jfly.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import io.spotnext.jfly.event.EventHandler;
import io.spotnext.jfly.event.Events.EventType;
import io.spotnext.jfly.util.KeyValueListMapping;

/**
 * Converts the key value mapping for event handlers to a JSON object that only
 * contains the event types
 */
public class EventHandlerSerializer extends StdSerializer<KeyValueListMapping<EventType, EventHandler>> {
	private static final long serialVersionUID = 1L;

	public EventHandlerSerializer() {
		this(null);
	}

	public EventHandlerSerializer(Class<KeyValueListMapping<EventType, EventHandler>> type) {
		super(type);
	}

	@Override
	public void serialize(KeyValueListMapping<EventType, EventHandler> value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException, JsonProcessingException {

		jgen.writeStartArray();

		// for (Map.Entry<EventType, List<EventHandler>> e : value.entrySet()) {
		// jgen.writeString(e.getKey().getInternalName());
		// }

		jgen.writeStartArray();
	}
}