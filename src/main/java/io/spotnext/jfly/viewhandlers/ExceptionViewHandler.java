package io.spotnext.jfly.viewhandlers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.spotnext.jfly.PageOrientedApplication;
import io.spotnext.jfly.ui.generic.GenericContainer;
import io.spotnext.jfly.ui.generic.HtmlTag;
import io.spotnext.jfly.ui.html.Body;
import io.spotnext.jfly.ui.html.Head;
import j2html.TagCreator;

public class ExceptionViewHandler extends PageOrientedApplication {

	@Override
	protected Head createHeader() {
		final Head head = new Head(getHandler()).setTitle("Error");

		return head;
	}

	@Override
	protected Body createBody() {
		Body body = new Body(this);

		GenericContainer errorHeader = new GenericContainer(this, "div");
		errorHeader.addStyleClass("cover");
		errorHeader.addChildren(new HtmlTag(this, TagCreator.h1("Sorry, an error occurred")));

		GenericContainer container = new GenericContainer(this, "div");
		container.addChildren(errorHeader);

		body.addChildren(container);

		return body;
	}

	@Override
	protected List<Locale> getSupportedLocales() {
		return Arrays.asList(Locale.ENGLISH);
	}

	@Override
	public void route(String url, boolean flushChanges) {
		//
	}
}
