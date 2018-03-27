package at.spot.jfly.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class DynamicEnum implements Enum {
	protected static final List<DynamicEnum> values = new ArrayList<>();
	protected String internalName;

	protected DynamicEnum(String internalName) {
		this.internalName = internalName;
		values.add(this);
	}

	public String getInternalName() {
		return internalName;
	}

	@Override
	public List<Enum> getValues() {
		return Collections.unmodifiableList(values);
	}

	@Override
	public String toString() {
		return getInternalName();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other instanceof DynamicEnum) {
			return StringUtils.equals(this.internalName, ((DynamicEnum) other).getInternalName());
		}

		return false;
	}
}