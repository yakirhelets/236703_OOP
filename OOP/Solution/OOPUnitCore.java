package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;

import java.util.Arrays;

public class OOPUnitCore {
    void assertEquals(Object expected, Object actual) throws OOPAssertionFailure {

    }
    void fail() throws OOPAssertionFailure {

    }


    OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        if (testClass == null || Arrays.asList(testClass.getClass().getAnnotations()).contains(OOPTestClass.class)) {
            throw new IllegalArgumentException();
        }
    }

    OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
        if (testClass == null || tag == null || Arrays.asList(testClass.getClass().getAnnotations()).contains(OOPTestClass.class)) {
            throw new IllegalArgumentException();
        }
    }
}
