package OOP.Solution;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target(METHOD)
public @interface OOPTest {
    int order(); // TODO: check if default is needed
    String tag() default "";

}
