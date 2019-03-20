package io.spotnext.jfly.templating.impl;

import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.implement.IncludeRelativePath;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.spotnext.jfly.templating.ComponentContext;
import io.spotnext.jfly.templating.TemplateService;
import io.spotnext.jfly.util.KeyValueMapping;

/**
 * This is a small velocity utility, that renders templates and objects.
 */
public class VelocityTemplateService implements TemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(VelocityTemplateService.class);

	private final VelocityEngine ve;
	private final String templateBasePath;

	private boolean isDebugEnabled = true;

	private KeyValueMapping<String, CachedTemplate> templateCache = new KeyValueMapping<>();

	private final Template COMPONENT_NOT_FOUND_TEMPLATE;

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

		String componentNotFoundTemplatePath = Paths.get(templateBasePath, "notFound.vm").toString();
		COMPONENT_NOT_FOUND_TEMPLATE = ve.getTemplate(componentNotFoundTemplatePath);

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
		final String templateFilePath = templateBasePath + "/" + templateFile;
		final long templateFileLastModifiedTimestamp = getLastModifiedDate(templateFilePath);

		CachedTemplate cachedEntry = templateCache.get(templateFile);

		if (cachedEntry != null) {
			if (cachedEntry.getLastModifiedTimestamp() < templateFileLastModifiedTimestamp) {
				// reset cached template
				cachedEntry = null;
			}
		}

		Template ret = null;

		if (cachedEntry == null) {
			final Optional<File> file = getAbsoluteTemplatePath(templateFilePath);

			if (file.isPresent()) {
				final Template tmpl = ve.getTemplate(templateFilePath);
				if (tmpl != null) {
					cachedEntry = new CachedTemplate(tmpl, templateFileLastModifiedTimestamp);
					templateCache.put(templateFile, cachedEntry);
					ret = tmpl;
				} else {
					ret = COMPONENT_NOT_FOUND_TEMPLATE;
				}
			}

		} else {
			ret = cachedEntry.getTemplate();
		}

		return ret;
	}

	protected Optional<File> getAbsoluteTemplatePath(String templateFilePath) {
		final URL resourceUrl = getClass().getResource(templateFilePath);

		if (resourceUrl != null) {
			try {
				return Optional.of(Paths.get(resourceUrl.toURI()).toFile());
			} catch (URISyntaxException e) {
				LOG.error(e.getMessage());
			}
		}

		return Optional.empty();
	}

	protected long getLastModifiedDate(String templateFilePath) {
		final Optional<File> file = getAbsoluteTemplatePath(templateFilePath);

		if (file.isPresent()) {
			return file.get().lastModified();
		}

		return -1;
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
