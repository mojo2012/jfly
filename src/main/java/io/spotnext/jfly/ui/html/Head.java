package io.spotnext.jfly.ui.html;

import java.util.ArrayList;
import java.util.List;

import io.spotnext.jfly.ComponentHandler;
import io.spotnext.jfly.ui.base.AbstractComponent;

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

	private void addDefaultStyles() {
		addStylesheet(new Stylesheet(getHandler(), "/style/deps.css"));
		addStylesheet(new Stylesheet(getHandler(), "/style/jfly.css"));
	}

	protected void addDefaultScripts() {
		// vue and other dependencies (merged into a single file)
		setScript(new Script(getHandler(), "/script/deps.js"));

		// jfly custom javascript
		setScript(new Script(getHandler(), "/script/jfly.js"));
	}

	public Head setTitle(final String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Head addStylesheet(final Stylesheet stylesheet) {
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
