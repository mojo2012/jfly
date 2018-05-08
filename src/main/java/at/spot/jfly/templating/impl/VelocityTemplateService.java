package at.spot.jfly.templating.impl;

import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.IncludeRelativePath;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.spot.jfly.templating.ComponentContext;
import at.spot.jfly.templating.TemplateService;
import at.spot.jfly.util.KeyValueMapping;

/**
 * This is a small velocity utility, that renders templates and objects.
 */
public class VelocityTemplateService implements TemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(VelocityTemplateService.class);

	private final VelocityEngine ve;
	private final String templateBasePath;

	private boolean isDebugEnabled = true;

	private KeyValueMapping<String, CachedTemplate> templateCache = new KeyValueMapping<>();

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
	public String render(final ComponentContext context, final String templateFile) {
		final Template tmpl = getTemplate(templateFile);

		final StringWriter writer = new StringWriter();

		if (isDebugEnabled()) {
			writer.write("<!-- START " + context.getComponent().getClass().getName() + " ("
					+ context.getComponent().getUuid() + ")" + " -->");
		}

		final VelocityContext ctx = new VelocityContext(context.getValues());
		tmpl.merge(ctx, writer);

		if (isDebugEnabled()) {
			writer.write("<!-- END " + context.getComponent().getClass().getName() + " -->");
		}

		return writer.toString();
	}

	/**
	 * Returns the content of the given template file. In case it is read the first
	 * time, or the file content has changed, the actual file is read and cached.
	 * All successive reads are returned from the cache.
	 */
	protected Template getTemplate(String templateFile) {
		final Path templateFilePath = Paths.get(templateBasePath, templateFile);
		final long templateFileLastModifiedTimestamp = getLastModifiedDate(templateFilePath);

		CachedTemplate cachedEntry = templateCache.get(templateFile);

		if (cachedEntry != null) {
			if (cachedEntry.getLastModifiedTimestamp() < templateFileLastModifiedTimestamp) {
				// reset cached template
				cachedEntry = null;
			}
		}

		if (cachedEntry == null) {
			final Template tmpl = ve.getTemplate(templateFilePath.toString());
			cachedEntry = new CachedTemplate(tmpl, templateFileLastModifiedTimestamp);
			templateCache.put(templateFile, cachedEntry);
		}

		return cachedEntry.getTemplate();
	}

	protected long getLastModifiedDate(Path templateFilePath) {
		return Paths.get(getClass().getResource(templateFilePath.toString()).getFile()).toFile().lastModified();
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		this.isDebugEnabled = isDebugEnabled;
	}

	protected class CachedTemplate {
		private Template template;
		private long lastModifiedTimestamp;

		public CachedTemplate(Template template, long lastModifiedTimestamp) {
			this.template = template;
			this.lastModifiedTimestamp = lastModifiedTimestamp;
		}

		public Template getTemplate() {
			return template;
		}

		public void setTemplate(Template template) {
			this.template = template;
		}

		public long getLastModifiedTimestamp() {
			return lastModifiedTimestamp;
		}

		public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
			this.lastModifiedTimestamp = lastModifiedTimestamp;
		}
	}
}
