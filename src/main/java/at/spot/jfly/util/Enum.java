package at.spot.jfly.util;

import java.util.List;

public interface Enum {
	String getInternalName();

	List<Enum> getValues();
}