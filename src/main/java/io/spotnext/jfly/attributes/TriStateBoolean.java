package io.spotnext.jfly.attributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spotnext.jfly.attributes.Attributes.Attribute;
import io.spotnext.jfly.util.Enum;

public enum TriStateBoolean implements Attribute {
	True("true"), False("false"), Intermediate("intermediate");

	private String internalName;

	private TriStateBoolean(final String internal) {
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
