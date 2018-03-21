package at.spot.jfly.style;

import io.gsonfire.annotations.ExposeMethodResult;

/**
 * The base interface for component modifiers.
 */
public interface Modifier {
	@ExposeMethodResult(value = "name")
	public String getName();

}
