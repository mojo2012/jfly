package io.spotnext.jfly.http.websocket;

public class ExceptionMessage extends Message {
	private String name;
	private String description;

	public ExceptionMessage() {
		super(MessageType.exception);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
