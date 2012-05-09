package pimp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Joel Harrison
 * 
 *         Annotation used to specify if a field should show up in the
 *         dynamically created form, also so can specify the label name that
 *         should dhow in the dynamically created form
 * 
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {
	String displayName() default NULL;

	public static final String NULL = "THIS IS A SPECIAL NULL VALUE - DO NOT USE";
}
