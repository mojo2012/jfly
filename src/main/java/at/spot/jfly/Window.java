package at.spot.jfly;

import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.html.Html;

public abstract class Window {

	private Html html;

	public Window() {
		html = new Html();
		html.head(createHeader());
		html.body(createBody());
	}

	protected abstract Head createHeader();

	protected abstract Body createBody();

	public Head head() {
		return html.head();
	}

	public Body body() {
		return html.body();
	}

	public String render() {
		return html.render();
	}

}
