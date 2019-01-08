package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPExceptionMismatchError;
import OOP.Provided.OOPExpectedException;
import OOP.Provided.OOPResult;
import OOP.Provided.OOPResult.OOPTestResult;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

public class OOPUnitCore {
    private OOPUnitCore(){} //private C'tor

    public static void assertEquals(Object expected, Object actual) throws OOPAssertionFailure {
        if(expected == null && actual == null){
            return;
        }
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
    //TODO finish and check this func
    private static void backupFields(Object original,Object fieldsBackup){
        Field[] fields = original.getClass().getDeclaredFields(); //get all test class fields
        for (Field field : fields) {
            try {
                Field fieldFrom = original.getClass().getDeclaredField(field.getName());
                fieldFrom.setAccessible(true);
                Object value = fieldFrom.get(original); //getting the original field value
                Object valueTo=null; //initialization
                if(value instanceof Cloneable) { //check if cloneable
                    valueTo = value.getClass().getMethod("clone").invoke(value);
                } else if(getCopyCons(value) != null){ //check if has a copy cons
                    Constructor<?> copyCons = getCopyCons(value); //get the copy cons
                    valueTo = copyCons.newInstance(value);

                } else{ //basic copy
                    valueTo = fieldFrom.get(original);
                }
                //common behavior for all cases = just put the copied value in its new place
                Field fieldTo = fieldsBackup.getClass().getDeclaredField(field.getName());
                fieldTo.setAccessible(true);
                fieldTo.set(fieldsBackup, valueTo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
        //check this function
    private static Constructor<?> getCopyCons(Object obj){
        for (Constructor<?> constructor : obj.getClass().getConstructors()) {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(obj.getClass())) {
                return constructor;
            }
        }
        return null;
    }

    //main function for running the BEFORE and the test method and AFTER for each *TEST method* - returns summary accord.
    private static OOPTestSummary runMethods(List<Class> classList,List<Method> testMethods,Object classInstance,Map<String, OOPResult> testMap,OOPExpectedException expected) throws IllegalAccessException, InstantiationException {
        List<Class> classRevList = new ArrayList<>(classList);
        Collections.reverse(classRevList); //from bottom to top! (for AFTER methods)
        //another object for backup class fields
        Object backupInstance = classInstance.getClass().newInstance();

        // run all BEFORE methods
        testMethods.forEach(testMethod -> {
            classList.stream().forEach(c ->
                    Arrays.stream(c.getMethods()).filter(beforeMethod -> beforeMethod.isAnnotationPresent(OOPBefore.class) // beforeMethod contains the "OOPBefore" annotation
                            && Stream.of(beforeMethod.getAnnotation(OOPBefore.class).value()).
                            anyMatch(methodName -> methodName.equals(testMethod.getName()))).
                            forEach(beforeMethod -> {
                                try {
                                    //backup fields
                                    backupFields(classInstance,backupInstance);
                                    beforeMethod.invoke(classInstance);
                                } catch (Exception e) {
                                    //restore backed up fields
                                    backupFields(backupInstance,classInstance);
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

            //reset expected for the next test
            if(expected != null) {
                expected.expect(null).expectMessage("");
            }

            // run all "AFTER METHODS" methods that are related to testMethod
            classRevList.stream().forEach(c ->
                    Arrays.stream(c.getMethods()).filter(afterMethod -> afterMethod.isAnnotationPresent(OOPAfter.class) // beforeMethod contains the "OOPBefore" annotation
                            && Stream.of(afterMethod.getAnnotation(OOPAfter.class).value()).
                            anyMatch(methodName -> methodName.equals(testMethod.getName()))).
                            forEach(afterMethod -> {
                                try {
                                    //backup fields
                                    backupFields(classInstance,backupInstance);
                                    afterMethod.invoke(classInstance);
                                } catch (Exception e) {
                                    //restore backed up fields
                                    backupFields(backupInstance,classInstance);
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
        String tag="";
        return runClass(testClass, tag);
    }




//------------------------------------------------------------------------------------------------------------
    //main method for running the test methods according to the annotations tagging
    public static OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
        if (testClass == null || tag == null || !testClass.isAnnotationPresent(OOPTestClass.class) ){
            throw new IllegalArgumentException();
        }

        //map for storing the test results for each method
        Map<String, OOPResult> testMap = new HashMap<String, OOPResult>();

        Object object = null; //class instance

        try {
            object = testClass;
            //making a class instance
            Object finalObject = ((Class) object).newInstance();
            //getting the expected exception variable
            final OOPExpectedException expected = getExpected(testClass, finalObject);
            List<Method> testMethods = getTestMethods(testClass,tag);
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
}
