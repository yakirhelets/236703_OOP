package OOP.Solution;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target(METHOD)
public @interface OOPBefore {
    String[] value(); //default {};
}
