package at.spot.jfly;

import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.html.Html;

public abstract class Window {

	private final Application application;
	private final Html html;

	public Window(final Application application) {
		this.application = application;
		html = new Html(application());
		html.setHead(createHeader());
		html.setBody(createBody());
	}

	/*
	 * Rendering.
	 */

	public Application application() {
		return this.application;
	}

	protected abstract Head createHeader();

	protected abstract Body createBody();

	public Head head() {
		return html.getHead();
	}

	public Body body() {
		return html.getBody();
	}

	public String render() {
		// Document doc = Jsoup.parseBodyFragment(html.render());
		//
		// return doc.outerHtml();

		return html.render();
	}

}
