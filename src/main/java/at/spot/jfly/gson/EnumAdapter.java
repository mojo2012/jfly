package at.spot.jfly.gson;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import at.spot.jfly.style.Style;

public class EnumAdapter extends TypeAdapter<Enum<? extends Style>> {

	@Override
	public Enum<? extends Style> read(final JsonReader in) throws IOException {
		// Enum<? extends Style> style = Enum.valueOf(enumType, name);
		//
		// in.
		//
		//
		// return style;

		return null;
	}

	@Override
	public void write(final JsonWriter out, final Enum<? extends Style> object) throws IOException {
		out.name(object.toString());
	}
}