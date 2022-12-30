package annotationen;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

@Target(METHOD)
public @interface CheckStringIO {

	String[] parameter();
	String[] returnValue();
}
