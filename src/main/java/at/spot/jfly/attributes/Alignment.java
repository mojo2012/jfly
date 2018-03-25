package at.spot.jfly.attributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.spot.jfly.attributes.Attributes.Attribute;
import at.spot.jfly.util.Enum;

public enum Alignment implements Attribute {
	Top("top"), Bottom("bottom"), Right("right"), Left("left"), Center("center");

	private String internalName;

	private Alignment(final String internal) {
		this.internalName = internal;
	}

	@Override
	public String getInternalName() {
		return this.internalName;
	}

	@Override
	public List<Enum> getValues() {
		return Stream.of(values()).collect(Collectors.toList());
	}
}
