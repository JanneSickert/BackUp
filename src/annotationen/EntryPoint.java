package annotationen;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

@Target(METHOD)
public @interface EntryPoint {

	String[] keys();
	String describtion();
}