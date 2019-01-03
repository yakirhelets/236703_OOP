package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    //TODO: should we merge the functions to get one overloaded function ??
    OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        if (testClass == null || !testClass.isAnnotationPresent(OOPTestClass.class) ) {
            throw new IllegalArgumentException();
        }
        //TODO work flow ->
        //check if class has ordered annotation and testClass annotation
        //find Exception rules
        //run setup mwthod
        //run all before for methods in its array
        //run OOPTest methods by order (if needed)
        //run all after for methods in its array
        //put all the results in the testMap below

        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>(); //map for storing the test results for each method
        //fill the map with the results
        OOPTestSummary testSummary = new OOPTestSummary(testMap);
        return testSummary;
    }


        //main method for running the test methods according to the annotations tagging
    OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
        if (testClass == null || tag == null || !testClass.isAnnotationPresent(OOPTestClass.class) ){
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
                if (m.getAnnotation(OOPTest.class).tag().equals(tag)) { // check if tags are the same
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
