package at.spot.jfly.attributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.spot.jfly.attributes.Attributes.Attribute;
import at.spot.jfly.util.Enum;

public enum Direction implements Attribute {
	Up("up"), Down("down"), Right("right"), Left("left");

	private String internalName;

	private Direction(final String internal) {
		this.internalName = internal;
	}

	public String getInternalName() {
		return this.internalName;
	}

	@Override
	public List<Enum> getValues() {
		return Stream.of(values()).collect(Collectors.toList());
	}
}
