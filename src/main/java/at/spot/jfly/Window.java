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
		html.head(createHeader());
		html.body(createBody());
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
		return html.head();
	}

	public Body body() {
		return html.body();
	}

	public String render() {
		// Document doc = Jsoup.parseBodyFragment(html.render());
		//
		// return doc.outerHtml();

		return html.render();
	}

}
