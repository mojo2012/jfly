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
		stylesheet(new Stylesheet(handler(), "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"));
		// stylesheet(new
		// Stylesheet("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"));

		return this;
	}

	protected Head addDefaultScripts() {
		// vue.js draws the client components
		script(new Script(handler(), "/script/vue-2.1.8.js"));
		// jquery alternative
		script(new Script(handler(), "http://zeptojs.com/zepto.min.js"));
		// bootstrap js
		// script(new
		// Script("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"));
		// custom code
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
