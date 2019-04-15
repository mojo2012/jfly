package io.spotnext.jfly.ui.base;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a draw command for the client to update the view.
 */
public class ClientUpdateCommand {
	private final transient LocalDateTime creationTimestamp = LocalDateTime.now();

	private DrawCommandType type;
	private String targetObject;
	private String function;
	private Object[] paramters;

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

	public LocalDateTime getCreationTimestamp() {
		return creationTimestamp;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClientUpdateCommand) {
			ClientUpdateCommand cu = (ClientUpdateCommand) obj;
			return type.equals(cu.getType()) //
					&& Objects.equals(targetObject, cu.getTargetObject()) //
					&& Objects.equals(function, cu.getFunction()) //
					&& Objects.equals(paramters, cu.getParamters());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, targetObject, function, paramters);
	}
}
