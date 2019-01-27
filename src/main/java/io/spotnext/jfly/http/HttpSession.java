package io.spotnext.jfly.http;

import java.util.Map;

import io.spotnext.jfly.util.KeyValueMapping;

public class HttpSession {
	private String id;
	private final KeyValueMapping<String, Object> attributes = new KeyValueMapping<>();

	public HttpSession(String id, Map<String, Object> attributes) {
		this.id = id;
		this.attributes.putAll(attributes);
	}

	public String getId() {
		return id;
	}

	public KeyValueMapping<String, Object> getAttributes() {
		return attributes;
	}

}
