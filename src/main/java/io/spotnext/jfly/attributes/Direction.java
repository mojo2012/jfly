package io.spotnext.jfly.attributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spotnext.jfly.attributes.Attributes.Attribute;
import io.spotnext.jfly.util.Enum;

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
