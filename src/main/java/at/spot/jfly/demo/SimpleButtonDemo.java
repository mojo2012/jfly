package at.spot.jfly.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.spot.jfly.Server;
import at.spot.jfly.ViewHandler;
import at.spot.jfly.attributes.Attributes.GridAlignment;
import at.spot.jfly.attributes.Attributes.GridBehavior;
import at.spot.jfly.ui.action.Button;
import at.spot.jfly.ui.display.Label;
import at.spot.jfly.ui.generic.GenericContainer;
import at.spot.jfly.ui.html.Body;
import at.spot.jfly.ui.html.Head;
import at.spot.jfly.util.Localizable;
import at.spot.jfly.util.LocalizationBundle;

public class SimpleButtonDemo extends ViewHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Server.class);

	private final static LocalizationBundle localizations = new LocalizationBundle("messages/demo_messages");

	@Override
	protected Head createHeader() {
		final Head head = new Head(getHandler()).setTitle("jfly UI framework");

		return head;
	}

	@Override
	protected Body createBody() {
		final Body body = new Body(getHandler());

		createMainContent(body);

		return body;
	}

	private void createMainContent(Body body) {
		GenericContainer mainContainer = new GenericContainer(getHandler(), "v-content");
		GenericContainer fluidContainer = new GenericContainer(getHandler(), "v-container");
		fluidContainer.addAttribute(GridBehavior.Fluid);
		fluidContainer.addAttribute(GridBehavior.FillHeight);
		final GenericContainer actualContainer = new GenericContainer(getHandler(), "v-layout");
		actualContainer.addAttribute(GridBehavior.JustifyCenter);
		actualContainer.addAttribute(GridAlignment.Center);
		actualContainer.setUseWrapper(true);
		actualContainer.addStyleClass("container");

		mainContainer.addChildren(fluidContainer);
		fluidContainer.addChildren(actualContainer);
		body.addChildren(mainContainer);

		Label label = new Label(getHandler());

		Button button = new Button(getHandler(), Localizable.of("Click me"));
		button.onClick(e -> {
			label.setText(Localizable.of("You clicked the button."));
		});

		fluidContainer.addChildren(label, button);

	}

	@Override
	protected List<Locale> getSupportedLocales() {
		return Arrays.asList(Locale.ENGLISH);
	}

	/**
	 * Entry point for demo application
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		final Server server = new Server(8080);
		server.registerViewHandler("/", SimpleButtonDemo.class);
		server.start();
	}

}
