package io.spotnext.jfly;

import java.util.Map;

import io.spotnext.jfly.http.HttpMethod;
import io.spotnext.jfly.http.HttpSession;
import io.spotnext.jfly.util.KeyValueMapping;

public class HttpRequest {

	private final KeyValueMapping<String, String> parameters = new KeyValueMapping<>();
	private final KeyValueMapping<String, String> cookies = new KeyValueMapping<>();

	private HttpMethod method;
	private String url;
	private HttpSession session;

	public HttpRequest(HttpMethod method, String url, Map<String, String> parameters, Map<String, String> cookies,
			HttpSession session) {

		this.method = method;
		this.url = url;
		this.parameters.putAll(parameters);
		this.cookies.putAll(cookies);
		this.session = session;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public KeyValueMapping<String, String> getParameters() {
		return parameters;
	}

	public KeyValueMapping<String, String> getCookies() {
		return cookies;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
