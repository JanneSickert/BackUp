package annotationen;

import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;

@Retention(CLASS)
public @interface ClassComment {

	String author() default "Janne";
	byte[] version();
	String comment() default "";
}