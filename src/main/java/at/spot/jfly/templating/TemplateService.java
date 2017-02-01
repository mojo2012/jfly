package at.spot.jfly.templating;

import java.util.Map;

public interface TemplateService {
	/**
	 * Renders the given template file with the given.
	 * 
	 * @param object
	 * @param templatePath
	 * @return
	 */
	public String render(Map<String, Object> object, String templateFilePath);
}
