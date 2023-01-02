package annotationen;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface ClassComment {

	String author() default "Janne";
	String version();
	String compilerVersion() default "";
	String comment() default "";
}