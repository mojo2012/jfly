package at.spot.jfly;

import java.util.Map;

import at.spot.jfly.http.HttpMethod;
import at.spot.jfly.http.HttpSession;
import at.spot.jfly.util.KeyValueMapping;

public class HttpRequest {

	private final KeyValueMapping<String, String> parameters = new KeyValueMapping<>();
	private final KeyValueMapping<String, String> cookies = new KeyValueMapping<>();

	private HttpMethod method;
	private HttpSession session;

	public HttpRequest(HttpMethod method, Map<String, String> parameters, Map<String, String> cookies,
			HttpSession session) {

		this.method = method;
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

}
