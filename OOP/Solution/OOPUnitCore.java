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


    public static OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        if (testClass == null || !testClass.isAnnotationPresent(OOPTestClass.class)) { //if not a TestClass or equals null -> throw exception
            throw new IllegalArgumentException();
        }

        //map for storing the test results for each method
        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>();
        Object object = null; //class instance

        //TODO creating a new class instance - we need to apply those methods on this instance!
        try {
//            Constructor<?> cons = testClass.getConstructor(testClass.getClass());
//            object = cons.newInstance();
            object = testClass;


            //getting the expected exception variable
            final OOPExpectedException[] expected = {null};
            Object finalObject = ((Class) object).newInstance();

            Arrays.stream(testClass.getDeclaredFields()).
                    filter(f -> f.isAnnotationPresent(OOPExceptionRule.class)).forEach(m -> {
                try {
                    // TODO : CHANGE THAT. NOT WORKING ON PRIVATE FIELD
                    m.setAccessible(true);
                    expected[0] = (OOPExpectedException) m.get(finalObject);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });

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

            //list of classes inheritance
            List<Class> classList = new ArrayList<Class>();
            Class currentClass = testClass;
//            System.out.println(currentClass.getName());
            while (currentClass != Object.class) { //check maybe we got here more than needed
                classList.add(currentClass);
                currentClass = currentClass.getSuperclass();
            }
            Collections.reverse(classList); //from top to bottom


            //RUN SETUP METHODS - *also need to run for father and so on*
            classList.forEach(c ->
                    Arrays.stream(c.getMethods()).filter(m -> m.isAnnotationPresent(OOPSetup.class)
                            && m.getDeclaringClass() != c).forEach(m -> {
                        try {
                            m.invoke(finalObject); // calling the setup methods from testClass
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    })
            );


            //run OOPTest methods by order (if needed)
            testMethods.forEach(testMethod -> { // run all OOPTest methods
                classList.stream().forEach(c ->
                        Arrays.stream(c.getMethods()).filter(beforeMethod -> beforeMethod.isAnnotationPresent(OOPBefore.class) // beforeMethod contains the "OOPBefore" annotation
                                && Stream.of(beforeMethod.getAnnotation(OOPBefore.class).value()).
                                anyMatch(methodName -> methodName.equals(testMethod.getName()))).
                                forEach(beforeMethod -> {
                                    try {
                                        beforeMethod.invoke(finalObject);
                                    } catch (Exception e) {
                                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getClass().getName()));
                                    }
                                })
                );

                try {
                    //CALL THE TEST METHOD
                    testMethod.invoke(finalObject);


                    if (expected[0] != null) { // WE EXPECTED AN EXCEPTION BUT DIDN'T GET ANY
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, expected[0].getClass().getName()));
                    } else { // if we arrived here, no exception were thrown => SUCCESS
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                    }
                } catch (OOPAssertionFailure e) {
                    testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.FAILURE, e.getMessage()));
                } catch (Exception e) {
                    if (expected[0].assertExpected(e)) { // case the exception thrown fit the expected
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                    } else {
                            if (expected[0].getExpectedException().equals(e)) { //DONT HAVE THE SAME MESSAGE
                                OOPExceptionMismatchError mismatch = new OOPExceptionMismatchError(expected[0].getExpectedException(), e.getClass());
                                testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, mismatch.getMessage()));
                            }
                            if (expected[0] == null) { //WE DIDN'T EXPECT TO GET AN EXCEPTION BUT WE GOT ONE
                                testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getClass().getName()));
                            }
                        }

                }

                // run all "AFTER METHODS" methods that are related to testMethod
                classList.stream().forEach(c ->
                        Arrays.stream(c.getMethods()).filter(afterMethod -> afterMethod.isAnnotationPresent(OOPAfter.class) // beforeMethod contains the "OOPBefore" annotation
                                && Stream.of(afterMethod.getAnnotation(OOPAfter.class).value()).
                                anyMatch(methodName -> methodName.equals(testMethod.getName()))).
                                forEach(afterMethod -> {
                                    try {
                                        afterMethod.invoke(finalObject);
                                    } catch (Exception e) {
                                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getClass().getName()));
                                    }
                                })
                );
            });
            //put all the results in the testMap below
            //fill the map with the results
            OOPTestSummary testSummary = new OOPTestSummary(testMap);
            return testSummary;

        } catch (Exception e) {
            //??
        }
        return null;
    }




//------------------------------------------------------------------------------------------------------------
    //main method for running the test methods according to the annotations tagging
    //TODO: fix according to the previous runClass()
    public static OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
//        if (testClass == null || tag == null || !testClass.isAnnotationPresent(OOPTestClass.class) ){
//            throw new IllegalArgumentException();
//        }
//        try {
//            Constructor<?> cons = testClass.getConstructor(testClass.getClass());
//            Object object = cons.newInstance(); //creating a new class instance
//
//            Object[] setupMethodsArray = Arrays.stream(testClass.getMethods()).
//                    filter(m -> m.isAnnotationPresent(OOPSetup.class)).toArray(); //SETUP methods array
//            for ( Method m : setupMethodsArray){
//                object.m(); //TODO fix
//            }
//            Object[] testMethodsArray = Arrays.stream(testClass.getMethods()).
//                    filter(m -> m.isAnnotationPresent(OOPTest.class)).toArray(); //SETUP methods array
//            for( Method m : testMethodsArray) { //run all method with the corresponding tag
//                if (m.getAnnotation(OOPTest.class).tag().equals(tag)) { // check if tags are the same
//                    //run all before of method m
//                    object.m(); //TODO fix
//                    //run all after of method m
//                }
//            }
//        }catch (Exception e) {
//            throw new IllegalArgumentException(); //TODO: what exception should we throw?
//        }
        return null; //TEMP
    }
}
