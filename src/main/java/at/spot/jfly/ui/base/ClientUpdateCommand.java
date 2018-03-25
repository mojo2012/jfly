package at.spot.jfly.ui.base;

/**
 * Represents a draw command for the client to update the view.
 */
public class ClientUpdateCommand {
	DrawCommandType type;
	String targetObject;
	String function;
	Object[] paramters;

	public ClientUpdateCommand(final DrawCommandType type, final String targetObject, final String function,
			final Object[] paramters) {

		super();
		this.type = type;
		this.targetObject = targetObject;
		this.function = function;
		this.paramters = paramters;
	}

	public DrawCommandType getType() {
		return type;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public String getFunction() {
		return function;
	}

	public Object[] getParamters() {
		return paramters;
	}
}
