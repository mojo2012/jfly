package io.spotnext.jfly.templating;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.spotnext.jfly.ui.base.AbstractComponent;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface Template {
	/**
	 * Use the given file as template for rendering.
	 */
	String useTemplateFilename() default "";

	/**
	 * Use the template of the given component for rendering.
	 */
	Class<? extends AbstractComponent> useTemplateOf() default AbstractComponent.class;
}
