package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPExceptionMismatchError;
import OOP.Provided.OOPExpectedException;
import OOP.Provided.OOPResult;
import OOP.Provided.OOPResult.OOPTestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

    //Helper Functions
    private static OOPExpectedException getExpected(Class<?> testClass, Object classInstance) {
        final OOPExpectedException[] expected = {null};
        Arrays.stream(testClass.getDeclaredFields()).
                filter(f -> f.isAnnotationPresent(OOPExceptionRule.class)).forEach(m -> {
                m.setAccessible(true);
            try {
                expected[0] = (OOPExpectedException) m.get(classInstance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return expected[0]; //NULL if nothing expected
    }

    private static List<Method> getTestMethods(Class<?> testClass,String tag){
        List<Method> testMethods = new ArrayList<Method>();
        //check if class has ordered annotation and testClass annotation.
        if (testClass.getAnnotation(OOPTestClass.class).value() == OOPTestClass.OOPTestClassType.ORDERED) {
            Arrays.stream(testClass.getMethods()).filter(m -> m.isAnnotationPresent(OOPTest.class) &&
                    m.getAnnotation(OOPTest.class).tag().equals(tag)).
                    sorted(Comparator.comparingInt(a -> a.getAnnotation(OOPTest.class).order())).
                    forEach(a -> testMethods.add(a)); //take the lowest order first
        } else { //UNORDERED
            Arrays.stream(testClass.getMethods()).filter(m -> m.isAnnotationPresent(OOPTest.class) &&
                    m.getAnnotation(OOPTest.class).tag().equals(tag)).
                    forEach(a -> testMethods.add(a));
        }
        return testMethods;
    }

    private static List<Class> getClassList(Class<?> testClass){
        List<Class> classList = new ArrayList<Class>();
        Class currentClass = testClass;
        while (currentClass != Object.class) {
            classList.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return classList;
    }

    private static void runSetupMethods(List<Class> classList,Object classInstance){
        //RUN SETUP METHODS
        classList.forEach(c ->
                Arrays.stream(c.getMethods()).filter(m -> m.isAnnotationPresent(OOPSetup.class)
                        && m.getDeclaringClass() != c).forEach(m -> {
                    try {
                        m.invoke(classInstance); // calling the setup methods from testClass
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                })
        );
    }

    //main function for running the BEFORE and the test method and AFTER for each *TEST method* - returns summary accord.
    private static OOPTestSummary runMethods(List<Class> classList,List<Method> testMethods,Object classInstance,Map<String, OOPResult> testMap,OOPExpectedException expected)
    {
        List<Class> classRevList = new ArrayList<>(classList);
        Collections.reverse(classRevList); //from bottom to top! (for AFTER methods)

        // run all BEFORE methods
        testMethods.forEach(testMethod -> {
            classList.stream().forEach(c ->
                    Arrays.stream(c.getMethods()).filter(beforeMethod -> beforeMethod.isAnnotationPresent(OOPBefore.class) // beforeMethod contains the "OOPBefore" annotation
                            && Stream.of(beforeMethod.getAnnotation(OOPBefore.class).value()).
                            anyMatch(methodName -> methodName.equals(testMethod.getName()))).
                            forEach(beforeMethod -> {
                                try {
                                    beforeMethod.invoke(classInstance);
                                } catch (Exception e) {
                                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getClass().getName()));
                                }
                            })
            );

            try {
                //CALL THE TEST METHOD
                testMethod.invoke(classInstance);

                if (expected != null && expected.getExpectedException() != null) { // WE EXPECTED AN EXCEPTION BUT DIDN'T GET ANY
                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, expected.getClass().getName()));
                } else { // if we arrived here, no exception were thrown => SUCCESS
                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                }
            }
            //TODO: check the logic here
            catch (InvocationTargetException e) {
                if (expected == null) { //WE DIDN'T EXPECT TO GET AN EXCEPTION BUT WE GOT ONE
                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getCause().getClass().getName()));
                }else { //WE DID EXPECT AN EXCEPTION
                    if(e.getCause().getClass().equals(OOPAssertionFailure.class)){ //ASSERTION FAILURE
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.FAILURE, e.getCause().getMessage()));
                    }
                    else if (expected.assertExpected((Exception)e.getCause())) { // case the exception thrown fit the expected
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                    }
                    else if (expected.getExpectedException().equals(e.getCause().getClass())) { //DONT HAVE THE SAME MESSAGE
                        OOPExceptionMismatchError mismatch = new OOPExceptionMismatchError(expected.getExpectedException(), e.getClass()); //TODO change!
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, mismatch.getMessage()));
                    }
                }
            } catch (Exception e) {
                //TODO what here?
            }

            // run all "AFTER METHODS" methods that are related to testMethod
            classRevList.stream().forEach(c ->
                    Arrays.stream(c.getMethods()).filter(afterMethod -> afterMethod.isAnnotationPresent(OOPAfter.class) // beforeMethod contains the "OOPBefore" annotation
                            && Stream.of(afterMethod.getAnnotation(OOPAfter.class).value()).
                            anyMatch(methodName -> methodName.equals(testMethod.getName()))).
                            forEach(afterMethod -> {
                                try {
                                    afterMethod.invoke(classInstance);
                                } catch (Exception e) {
                                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getClass().getName()));
                                }
                            })
            );
        });

        //fill the map with the results
        OOPTestSummary testSummary = new OOPTestSummary(testMap);
        return testSummary;
    }


    public static OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        if (testClass == null || !testClass.isAnnotationPresent(OOPTestClass.class)) {
            throw new IllegalArgumentException(); //if not a TestClass or equals null -> throw exception
        }

        //map for storing the test results for each method
        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>();
        String noTag="";

        Object object = null; //class instance

        try {
            object = testClass;
            //making a class instance
            Object finalObject = ((Class) object).newInstance();
            //getting the expected exception variable
            final OOPExpectedException expected = getExpected(testClass, finalObject);
            List<Method> testMethods = getTestMethods(testClass,noTag);
            //list of classes inheritance
            List<Class> classList = getClassList(testClass);
            Collections.reverse(classList); //from top to bottom

            //RUN SETUP METHODS - *also need to run for father and so on*
            runSetupMethods(classList,finalObject);

            //RUN MAIN FUNCTION
            return runMethods(classList,testMethods,finalObject,testMap,expected);
        } catch (Exception e) {
            //TODO what here?
        }
        return null;
    }




//------------------------------------------------------------------------------------------------------------
    //main method for running the test methods according to the annotations tagging
    //TODO: fix according to the previous runClass()
    public static OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
        if (testClass == null || tag == null || !testClass.isAnnotationPresent(OOPTestClass.class) ){
            throw new IllegalArgumentException();
        }

        return null; //TEMP
    }
}
