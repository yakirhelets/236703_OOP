package OOP.Solution;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target(METHOD)
public @interface OOPSetup {
    boolean wasCalled() default false; // TODO: check if using correctly
}
