package annotationen;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// A kind of project-internal JavaDoc as annotation.

@Retention( RetentionPolicy.RUNTIME )
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Comment {

	String make() default "nothing specified";		// A description of what the method does
	String[] param() default "nothing specified";	// parameter
	String ret() default "nothing specified";		// return value
}