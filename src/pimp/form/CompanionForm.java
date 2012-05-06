package pimp.form;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CompanionForm {
	String form() default NULL;  
	public static final String NULL = "THIS IS A SPECIAL NULL VALUE - DO NOT USE";
}
