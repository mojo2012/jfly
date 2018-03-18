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
		setStylesheet(new Stylesheet(getHandler(),
				"/style/jfly.css"));
		setStylesheet(new Stylesheet(getHandler(),
				"/style/vuetify-custom.css"));
		setStylesheet(new Stylesheet(getHandler(),
				"https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Material+Icons"));
		setStylesheet(new Stylesheet(getHandler(), "https://unpkg.com/vuetify/dist/vuetify.min.css"));

		return this;
	}

	protected Head addDefaultScripts() {
		// vue.js draws the client components
		setScript(new Script(getHandler(), "https://unpkg.com/vue/dist/vue.js"));

		setScript(new Script(getHandler(), "https://code.jquery.com/jquery-3.1.1.min.js"));
		setScript(new Script(getHandler(), "https://unpkg.com/vuetify/dist/vuetify.js"));

		// jfly custom javascript
		setScript(new Script(getHandler(), "/script/jfly.js"));

		return this;
	}

	public Head setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Head setStylesheet(final Stylesheet stylesheet) {
		stylesheets.add(stylesheet);
		return this;
	}

	public List<Stylesheet> getStylesheets() {
		return stylesheets;
	}

	public Head setScript(final Script script) {
		scripts.add(script);
		return this;
	}

	public List<Script> getScripts() {
		return scripts;
	}
}
