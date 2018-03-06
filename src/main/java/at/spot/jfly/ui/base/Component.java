package at.spot.jfly.ui.base;

import java.util.List;

import at.spot.jfly.util.GsonUtil;

public interface Component {

	/**
	 * This unique identifier is used to identify the component on the client
	 * and bind it to the server-side component instance.
	 */
	String getUuid();

	/**
	 * Renders the raw j2html item - accessible with {@link #raw()}.
	 */
	String render();

	/**
	 * Redraws the component on the client side.
	 * 
	 * @return
	 */
	<C extends AbstractComponent> C redraw();

	/**
	 * Serializes the component's state into a json string.
	 */
	default String toJson() {
		return GsonUtil.toJson(this);
	}

	/**
	 * Returns true, if the component needs to be redrawn.
	 * 
	 * @return
	 */
	boolean needsRedraw();

	/**
	 * This is an ordered list of draw commands. First added is on top.
	 * 
	 * @return
	 */
	List<DrawCommand> getDrawCommands();

	void clearDrawCommands();
}
