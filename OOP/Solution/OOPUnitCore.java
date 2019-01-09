package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPExceptionMismatchError;
import OOP.Provided.OOPExpectedException;
import OOP.Provided.OOPResult;
import OOP.Provided.OOPResult.OOPTestResult;

import javax.swing.plaf.metal.MetalToggleButtonUI;
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
        if(!expected.equals(actual)){
            throw new OOPAssertionFailure();
        }
    }

    public static void fail() throws OOPAssertionFailure {
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

    //getting all the test methods from all the related classes
    private static List<Method> getTestMethods(Class<?> testClass,String tag){
        List<Method> testMethods = new ArrayList<Method>();
        String emptyTag=""; //in case we don't have restriction on the method tag
        //check if class has ordered annotation and testClass annotation.
        if (testClass.getAnnotation(OOPTestClass.class).value() == OOPTestClass.OOPTestClassType.ORDERED) {
            Arrays.stream(testClass.getMethods()).filter(m -> m.isAnnotationPresent(OOPTest.class) &&
                    (tag.equals(emptyTag) || m.getAnnotation(OOPTest.class).tag().equals(tag))).
                    sorted(Comparator.comparingInt(a -> a.getAnnotation(OOPTest.class).order())).
                    forEach(a -> testMethods.add(a)); //take the lowest order first
        } else { //UNORDERED
            Arrays.stream(testClass.getMethods()).filter(m -> m.isAnnotationPresent(OOPTest.class) &&
                    (tag.equals(emptyTag) || m.getAnnotation(OOPTest.class).tag().equals(tag))).
                    forEach(a -> testMethods.add(a));
        }
        return testMethods;
    }

    //getting all of the given class inheritance tree of classes
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
        Class setupClass =classList.get(classList.size()-1);
        List<Method> setupClassMethods = Arrays.asList(setupClass.getMethods());
        Collections.reverse(setupClassMethods);
        setupClassMethods.stream().filter(m -> m.isAnnotationPresent(OOPSetup.class)
                        && ( m.getDeclaringClass() != setupClass || //SETUP overriden
                        Arrays.stream(setupClass.getDeclaredMethods()).anyMatch(func -> func.equals(m))) )
                        .forEach(m -> {
                    try {
                        m.invoke(classInstance); // calling the setup methods from testClass
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                })
        ;
    }

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
                    copyCons.setAccessible(true);
                    valueTo = copyCons.newInstance(value);

                } else{ //basic copy
                    valueTo = fieldFrom.get(original);
                }
                //common behavior for all cases = just put the copied value in its new place
                Field fieldTo = fieldsBackup.getClass().getDeclaredField(field.getName());
                fieldTo.setAccessible(true);
                fieldTo.set(fieldsBackup, valueTo);
            } catch (Exception e) {
                e.printStackTrace(); //TODO check here
            }
        }
    }

    private static Constructor<?> getCopyCons(Object obj){
        for (Constructor<?> constructor : obj.getClass().getConstructors()) {
            Type[] parameterTypes = constructor.getGenericParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].equals(obj.getClass())) {
                return constructor;
            }
        }
        return null;
    }

    private static void initExpected(OOPExpectedException expected){
        //reset expected for the next test
        if(expected != null) {
            expected.expect(null).expectMessage("");
        }
    }

    private static void runBeforeMethos(Method testMethod,Class beforeClass,Map<String, OOPResult> testMap,Object classInstance,Object backupInstance){
        List<Method> beforeClassMethods = Arrays.asList(beforeClass.getMethods());
        Collections.reverse(beforeClassMethods);
        beforeClassMethods.stream().filter(beforeMethod -> beforeMethod.isAnnotationPresent(OOPBefore.class) // beforeMethod contains the "OOPBefore" annotation
                && Stream.of(beforeMethod.getAnnotation(OOPBefore.class).value()).
                anyMatch(methodName -> methodName.equals(testMethod.getName()))
                && ( beforeMethod.getDeclaringClass() != beforeClass || //BEFORE overriden
                Arrays.stream(beforeClass.getDeclaredMethods()).anyMatch(func -> func.equals(beforeMethod)))
        ).forEach(beforeMethod -> {
            backupAndInvoke(testMethod,beforeMethod,classInstance,backupInstance,testMap);
        });
    }

    private static void runAfterMethos(Method testMethod,Class afterClass,Map<String, OOPResult> testMap,Object classInstance,Object backupInstance) {
        Arrays.stream(afterClass.getMethods()).filter(afterMethod -> afterMethod.isAnnotationPresent(OOPAfter.class) // beforeMethod contains the "OOPBefore" annotation
                && Stream.of(afterMethod.getAnnotation(OOPAfter.class).value()).
                anyMatch(methodName -> methodName.equals(testMethod.getName()))).
                forEach(afterMethod -> {
                    backupAndInvoke(testMethod,afterMethod,classInstance,backupInstance,testMap);
                });
    }

    //backup class field and invoke the given function, if we got an error -> restore the fields and put ERROR in the result map
    private static void backupAndInvoke(Method testMethod,Method methodToInvoke,Object classInstance,Object backupInstance,Map<String, OOPResult> testMap){
        try {
            //backup fields
            backupFields(classInstance, backupInstance);
            methodToInvoke.invoke(classInstance);
        } catch (Exception e) {
            //restore backed up fields
            backupFields(backupInstance, classInstance);
            testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getClass().getName()));
        }
    }



        //main function for running the BEFORE and the test method and AFTER for each *TEST method* - returns summary accord.
    private static OOPTestSummary runMethods(List<Class> classList,List<Method> testMethods,Object classInstance,Map<String, OOPResult> testMap,OOPExpectedException expected) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Class> classRevList = new ArrayList<>(classList);
        Collections.reverse(classRevList); //from bottom to top! (for AFTER methods)
        Class afterClass = classRevList.get(0);

        //another object for backup class fields
        Constructor<?> constructor= classInstance.getClass().getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Object backupInstance = constructor.newInstance();
        //Object backupInstance = classInstance.getClass().newInstance();
        Class beforeClass =classList.get(classList.size()-1);

        //for each TEST METHOD DO:
        testMethods.forEach(testMethod -> {

            // run all BEFORE methods
            runBeforeMethos(testMethod,beforeClass,testMap,classInstance,backupInstance);

            //no error accrued during the BEFORE methods => continue
            if(!(testMap.get(testMethod.getName()) != null && testMap.get(testMethod.getName()).getResultType().equals(OOPTestResult.ERROR))) {

                try {
                    //CALL THE TEST METHOD
                    testMethod.invoke(classInstance);

                    //no exception was thrown ->
                    if (expected != null && expected.getExpectedException() != null) { // WE EXPECTED AN EXCEPTION BUT DIDN'T GET ANY
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, expected.getClass().getName()));
                    } else { // if we arrived here, no exception were thrown => SUCCESS
                        testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                    }
                }
                //exception *was* thrown ->
                catch (InvocationTargetException e) {
                    if (expected == null) { //WE DIDN'T EXPECT TO GET AN EXCEPTION BUT WE GOT ONE
                        if (e.getCause().getClass().equals(OOPAssertionFailure.class)) {
                            testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.FAILURE, e.getCause().getMessage()));
                        } else {
                            testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.ERROR, e.getCause().getClass().getName()));
                        }
                    } else { //WE DID EXPECT AN EXCEPTION
                        if (e.getCause().getClass().equals(OOPAssertionFailure.class)) { //ASSERTION FAILURE
                            testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.FAILURE, e.getCause().getMessage()));
                        } else if (expected.assertExpected((Exception) e.getCause())) { // case the exception thrown fit the expected
                            testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                        } else if (expected.getExpectedException().equals(e.getCause().getClass()) ||
                                !expected.getExpectedException().equals(e.getCause().getClass())) {
                            //DONT HAVE THE SAME MESSAGE or WE GOT DIFFERENT EXCEPTION THAN EXPECTED
                            OOPExceptionMismatchError mismatch = new OOPExceptionMismatchError(expected.getExpectedException(), e.getClass()); //TODO change!
                            testMap.put(testMethod.getName(), new OOPResultImpl(OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, mismatch.getMessage()));
                        }
                    }
                } catch (Exception e) {
                    //TODO what here?
                }

                //reset expected value for the next test
                initExpected(expected);

                // run all "AFTER METHODS" that are related to testMethod
                runAfterMethos(testMethod,afterClass,testMap,classInstance,backupInstance);

            }
        });

        //fill the map with the results
        OOPTestSummary testSummary = new OOPTestSummary(testMap);
        return testSummary;
    }

    public static OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        String tag="";
        return runClass(testClass, tag);
    }


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
            Constructor<?> constructor= testClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            Object finalObject = constructor.newInstance();

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
