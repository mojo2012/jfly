package at.spot.jfly.viewhandlers;

import at.spot.jfly.ViewHandler;
import at.spot.jfly.ui.generic.GenericContainer;
import at.spot.jfly.ui.generic.HtmlTag;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import j2html.TagCreator;

public class ExceptionViewHandler extends ViewHandler {

	@Override
	protected Head createHeader() {
		final Head head = new Head(getHandler()).setTitle("Error");

		return head;
	}

	@Override
	protected Body createBody() {
		Body body = new Body(this);

		GenericContainer errorHeader = new GenericContainer(this, "div");
		errorHeader.addStyleClasses("cover");
		errorHeader.addChildren(new HtmlTag(this, TagCreator.h1("Sorry, an error occurred")));

		GenericContainer container = new GenericContainer(this, "div");
		container.addChildren(errorHeader);

		body.addChildren(container);

		return body;
	}

}
