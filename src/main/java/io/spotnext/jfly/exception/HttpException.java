package io.spotnext.jfly.exception;

public class HttpException extends Exception {
	private static final long serialVersionUID = 1L;
	private final Object httpStatusCode;

	public HttpException(final int httpStatusCode) {
		super("HTTP status " + httpStatusCode);
		this.httpStatusCode = httpStatusCode;
	}

	public Object getHttpStatusCode() {
		return httpStatusCode;
	}

}
