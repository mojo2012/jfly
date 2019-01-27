package io.spotnext.jfly.templating;

public interface TemplateService {
	/**
	 * Renders the given template file with the given.
	 * 
	 * @param currentWebSocketSession
	 * @param templatePath
	 * @return
	 */
	public String render(ComponentContext context, String templateFile);
}
