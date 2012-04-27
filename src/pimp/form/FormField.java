package pimp.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormField {
	String displayName() default NULL;  
	public static final String NULL = "THIS IS A SPECIAL NULL VALUE - DO NOT USE";
}
