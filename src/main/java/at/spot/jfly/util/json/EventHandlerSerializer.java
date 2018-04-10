package at.spot.jfly.util.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import at.spot.jfly.event.EventHandler;

public class EventHandlerSerializer extends StdSerializer<EventHandler> {
	private static final long serialVersionUID = 1L;

	public EventHandlerSerializer() {
		this(null);
	}

	public EventHandlerSerializer(Class<EventHandler> type) {
		super(type);
	}

	@Override
	public void serialize(EventHandler value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		jgen.writeString("handleEvent($event.type, $event.target, $event)");
	}
}