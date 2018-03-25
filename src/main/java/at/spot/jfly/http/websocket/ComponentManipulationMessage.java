package at.spot.jfly.http.websocket;

public class ComponentManipulationMessage extends Message {
	private String componentUuid;
	private String method;
	private Object[] parameters;

	public ComponentManipulationMessage() {
		super(MessageType.componentManipulation);
	}

	public String getComponentUuid() {
		return componentUuid;
	}

	public void setComponentUuid(String componentUuid) {
		this.componentUuid = componentUuid;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

}
