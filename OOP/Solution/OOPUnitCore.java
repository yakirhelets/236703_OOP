package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class OOPUnitCore {
    private OOPUnitCore(){} //private C'tor

    static void assertEquals(Object expected, Object actual) throws OOPAssertionFailure {
        if(expected == null || actual == null ){
            throw new OOPAssertionFailure();
        }
        //should we check if they both got the same class?
        if(!expected.equals(actual)){
            throw new OOPAssertionFailure();
        }
    }

    static void fail() throws OOPAssertionFailure { //throws OOPAssertionFauilure Exception
        throw new OOPAssertionFailure();
    }


    OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        if (testClass == null || testClass.isAnnotationPresent(OOPTestClass.class) ) {
            throw new IllegalArgumentException();
        }
    }

    OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
        if (testClass == null || tag == null || testClass.isAnnotationPresent(OOPTestClass.class) ){
            throw new IllegalArgumentException();
        }
        try {
            Constructor<?> cons = testClass.getConstructor(testClass.getClass());
            Object object = cons.newInstance(); //creating a new class instance

            Object[] setupMethodsArray = Arrays.stream(testClass.getMethods()).
                    filter(m -> m.isAnnotationPresent(OOPSetup.class)).toArray(); //SETUP methods array
            for ( Method m : setupMethodsArray){
                object.m(); //TODO fix
            }
            Object[] testMethodsArray = Arrays.stream(testClass.getMethods()).
                    filter(m -> m.isAnnotationPresent(OOPTest.class)).toArray(); //SETUP methods array
            for( Method m : testMethodsArray) { //run all method with the corresponding tag
                if (m.getAnnotation(OOPTest.class).tag == tag) {
                    //run all before of method m
                    object.m(); //TODO fix
                    //run all after of method m
                }
            }
        }catch (Exception e) {
            throw new IllegalArgumentException(); //TODO: what exception should we throw?
        }
    }
}
