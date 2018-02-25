package at.spot.jfly.ui.html;

import java.util.ArrayList;
import java.util.List;

import at.spot.jfly.ComponentHandler;
import at.spot.jfly.ui.base.AbstractComponent;

public class Head extends AbstractComponent {

	protected String title;
	protected List<Stylesheet> stylesheets = new ArrayList<>();
	protected List<Script> scripts = new ArrayList<>();

	public Head(final ComponentHandler handler) {
		super(handler);
		// necessary for the default components
		addDefaultStyles();
		addDefaultScripts();
	}

	private Head addDefaultStyles() {
		stylesheet(new Stylesheet(handler(), "https://www.w3schools.com/w3css/4/w3.css"));
		stylesheet(new Stylesheet(handler(),
				"https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons"));
		stylesheet(new Stylesheet(handler(), "https://unpkg.com/vuetify/dist/vuetify.min.css"));

		return this;
	}

	protected Head addDefaultScripts() {
		// vue.js draws the client components
		script(new Script(handler(), "https://unpkg.com/vue/dist/vue.js"));

		script(new Script(handler(), "https://code.jquery.com/jquery-3.1.1.min.js"));
		script(new Script(handler(), "https://unpkg.com/vuetify/dist/vuetify.js"));

		// jfly custom javascript
		script(new Script(handler(), "/script/jfly.js"));

		return this;
	}

	public Head title(final String title) {
		this.title = title;
		return this;
	}

	public String title() {
		return title;
	}

	public Head stylesheet(final Stylesheet stylesheet) {
		stylesheets.add(stylesheet);
		return this;
	}

	public List<Stylesheet> stylesheets() {
		return stylesheets;
	}

	public Head script(final Script script) {
		scripts.add(script);
		return this;
	}

	public List<Script> scripts() {
		return scripts;
	}
}
