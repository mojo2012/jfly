package io.spotnext.jfly.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.spotnext.jfly.Server;
import io.spotnext.jfly.ViewHandler;
import io.spotnext.jfly.attributes.Attributes.GridLayoutSize;
import io.spotnext.jfly.ui.action.Button;
import io.spotnext.jfly.ui.display.Label;
import io.spotnext.jfly.ui.html.Body;
import io.spotnext.jfly.ui.html.Head;
import io.spotnext.jfly.ui.layout.GridLayout;
import io.spotnext.jfly.util.Localizable;

public class SimpleButtonDemo extends ViewHandler {
	@Override
	protected Head createHeader() {
		return new Head(getHandler()).setTitle("jfly UI framework");
	}

	@Override
	protected Body createBody() {
		final Body body = new Body(getHandler());

		createMainContent(body);

		return body;
	}

	private void createMainContent(Body body) {
		GridLayout mainContainer = new GridLayout(getHandler());
		mainContainer.setSize(GridLayoutSize.XS1);

		body.addChildren(mainContainer);

		Label label = new Label(getHandler());

		Button button = new Button(getHandler(), Localizable.of("Click me"));
		button.onClick(e -> {
			label.setText(Localizable.of("You clicked the button."));
		});

		mainContainer.addChildren(label, button);
	}

	@Override
	protected List<Locale> getSupportedLocales() {
		return Arrays.asList(Locale.ENGLISH);
	}

	/**
	 * Entry point for demo application
	 */
	public static void main(final String[] args) {
		final Server server = new Server(8080);
		server.registerViewHandler("/", SimpleButtonDemo.class);
		server.start();
	}

}
