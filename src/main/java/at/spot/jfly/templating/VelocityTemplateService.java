package at.spot.jfly.templating;

import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.IncludeRelativePath;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a small velocity utility, that renders templates and objects.
 */
public class VelocityTemplateService implements TemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(VelocityTemplateService.class);

	private final VelocityEngine ve;
	private final String templateBasePath;

	public VelocityTemplateService(final String templateBasePath) {
		ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		ve.setProperty("runtime.log.logsystem.log4j.logger", LOG.getName());
		ve.setProperty(Velocity.RESOURCE_LOADER, "string");
		ve.addProperty("string.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.addProperty("string.resource.loader.repository.static", "false");
		ve.addProperty("string.resource.loader.modificationCheckInterval", "1");
		ve.addProperty(RuntimeConstants.EVENTHANDLER_INCLUDE, IncludeRelativePath.class.getName());
		ve.init();
		ve.init();

		this.templateBasePath = templateBasePath;
	}

	@Override
	public String render(final Map<String, Object> context, final String templateFile) {
		final Template tmpl = ve.getTemplate(Paths.get(templateBasePath, templateFile).toString());

		final StringWriter writer = new StringWriter();

		final VelocityContext ctx = new VelocityContext(context);
		tmpl.merge(ctx, writer);

		return writer.toString();
	}
}
