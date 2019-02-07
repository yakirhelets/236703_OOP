package OOP.Solution;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import static OOP.Solution.OOPTestClass.OOPTestClassType.*; //TODO: check if we can use static import
import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OOPTestClass {
    enum OOPTestClassType {
        UNORDERED, ORDERED
    }
    OOPTestClassType value() default UNORDERED;
}
