package io.spotnext.jfly.http.websocket;

public class FunctionCallMessage extends Message {
	private String object;
	private String functionCall;
	private Object[] parameters;

	public FunctionCallMessage() {
		super(MessageType.functionCall);
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getFunctionCall() {
		return functionCall;
	}

	public void setFunctionCall(String functionCall) {
		this.functionCall = functionCall;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

}
