package pimp.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Joel Harrison
 * 
 *         Annotation for specifying if a product should use a companion form
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CompanionForm {
	String form() default NULL;

	public static final String NULL = "THIS IS A SPECIAL NULL VALUE - DO NOT USE";
}
