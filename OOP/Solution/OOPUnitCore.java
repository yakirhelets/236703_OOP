package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPExpectedException;
import OOP.Provided.OOPResult;
import OOP.Provided.OOPResult.OOPTestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OOPUnitCore {
    private OOPUnitCore(){} //private C'tor

    public static void assertEquals(Object expected, Object actual) throws OOPAssertionFailure {
        if(expected == null || actual == null ){
            throw new OOPAssertionFailure();
        }
        //should we check if they both got the same class?
        if(!expected.equals(actual)){
            throw new OOPAssertionFailure();
        }
    }

    public static void fail() throws OOPAssertionFailure { //throws OOPAssertionFauilure Exception
        throw new OOPAssertionFailure();
    }

    //TODO: should we merge the functions to get one overloaded function ??
    public static OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        if (testClass == null || !testClass.isAnnotationPresent(OOPTestClass.class) ) { //if not a TestClass or equals null -> throw exception
            throw new IllegalArgumentException();
        }

        //map for storing the test results for each method
        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>();

        //TODO creating a new class instance - we need to apply those methods on this instance!
        Constructor<?> cons = testClass.getConstructor(testClass.getClass());
        Object object = cons.newInstance();

        List<Method> testMethods = new ArrayList<Method>(); //array list for testMethods!

        //check if class has ordered annotation and testClass annotation.
        if (testClass.getAnnotation(OOPTestClass.class).value() == OOPTestClass.OOPTestClassType.ORDERED) {
            Arrays.stream(testClass.getMethods()).filter(m -> m.isAnnotationPresent(OOPTest.class)).
                    sorted(Comparator.comparingInt(a -> a.getAnnotation(OOPTest.class).order())).
                    forEach(a -> testMethods.add(a)); //take the lowest order first
        } else { //UNORDERED
            Arrays.stream(testClass.getMethods()).filter(m -> m.isAnnotationPresent(OOPTest.class)).
                    forEach(a -> testMethods.add(a));
        }

        //TODO: find Exception rules

        //run setup method - *also need to run for father and so on*
        Arrays.stream(testClass.getMethods()).filter(m -> m.isAnnotationPresent(OOPSetup.class)).forEach(m -> {
            try {
                m.invoke(testClass); // calling the setup methods from testClass
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        //run OOPTest methods by order (if needed)
        // TODO: apply order. Should probably add "sort()" to the stream
        Arrays.stream(testClass.getMethods()).filter(method -> method.isAnnotationPresent(OOPTest.class)).forEach(testMethod -> { // run all OOPTest methods
            try {
                // run all "before" methods that are related to testMethod
                Arrays.stream(testClass.getMethods()).filter(beforeMethod -> beforeMethod.isAnnotationPresent(OOPBefore.class) // beforeMethod contains the "OOPBefore" annotation
                && Stream.of(beforeMethod.getAnnotation(OOPBefore.class).value()).anyMatch(methodName -> methodName.equals(testMethod.getName()))).forEach(beforeMethod -> { // beforeMethod contains "testMethod" in the "value" field
                    try {
                        beforeMethod.invoke(testClass);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        //we should check what kind of exception we got here and compare it to ExpectedException if available
                        e.printStackTrace();
                    }
                });

                testMethod.invoke(testClass); // calling the test methods from testClass
                // if we arrived here, no exception were thrown => SUCCESS
                testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));

                // run all "after" methods that are related to testMethod
                Arrays.stream(testClass.getMethods()).filter(afterMethod -> afterMethod.isAnnotationPresent(OOPAfter.class) // afterMethod contains the "OOPBefore" annotation
                        && Stream.of(afterMethod.getAnnotation(OOPAfter.class).value()).anyMatch(methodName -> methodName.equals(testMethod.getName()))).forEach(afterMethod -> { // afterMethod contains "testMethod" in the "value" field
                    try {
                        afterMethod.invoke(testClass);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
                // analyzing the test result
                // TODO: add all cases of exceptions
            } catch (Exception e) { // case of SUCCESS
                if (OOPExpectedException.none().assertExpected(e)) { // case the exception thrown fit the expected
                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                } else {
                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.FAILURE, e.getMessage()));
                }
            }
        });
        //put all the results in the testMap below


        //fill the map with the results
        OOPTestSummary testSummary = new OOPTestSummary(testMap);
        return testSummary;
    }


    //main method for running the test methods according to the annotations tagging
    //TODO: fix according to the previous runClass()
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
