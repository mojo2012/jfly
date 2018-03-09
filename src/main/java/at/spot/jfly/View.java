package at.spot.jfly;

import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.ui.html.Html;

public abstract class View {

	private final Application handler;
	private final Html html;

	public View(final Application handler) {
		this.handler = handler;
		html = new Html(getHandler());
		html.setHead(createHeader());
		html.setBody(createBody());
	}

	/*
	 * Rendering.
	 */

	public Application getHandler() {
		return this.handler;
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
