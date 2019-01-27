package io.spotnext.jfly.attributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spotnext.jfly.attributes.Attributes.Attribute;
import io.spotnext.jfly.util.Enum;

public enum NavigationTarget implements Attribute {
	Blank("_blank"), Self("_self"), Parent("_parent"), Top("_top");

	private String internalName;

	private NavigationTarget(final String internName) {
		this.internalName = internName;
	}

	public String getInternalName() {
		return this.internalName;
	}

	@Override
	public List<Enum> getValues() {
		return Stream.of(values()).collect(Collectors.toList());
	}

}
