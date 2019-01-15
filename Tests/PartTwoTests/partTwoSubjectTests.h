#ifndef OOP5_SERG_TEST_FOUR
#define OOP5_SERG_TEST_FOUR

#include ".\..\BaseTestFile.h"
#include ".\..\..\part2\Observer.h"
#include ".\..\..\part2\Subject.h"

int test = 0;
class SubTestInt : public Subject<int> {};
class ObsTestIntTwo : public Observer<int> {
private:
public:
    ObsTestIntTwo() : Observer(){
    }
    int getTest() { return test; }
    void handleEvent(const int& msg) override {
       test++;
    }
};

bool testAddingAnObserver(){
    PRINT_TEST_RUN("testAddingAnObserver");
    
    ObsTestIntTwo obi1,obi2;
    SubTestInt sub,sub2;
    try{
      sub.addObserver(obi1);
      sub += obi2;
      return true;
    } catch(...){
      std::cout << std::endl << "thrown an Exception" << std::endl;
      return false;
    }
}

bool testAddingSameObserverOperator(){
    PRINT_TEST_RUN("testAddingSameObserverOperator");
    
    ObsTestIntTwo obi1;
    SubTestInt sub,sub2;

    try{
       (sub += obi1) += obi1;
       std::cout << std::endl << "Added same Observer ... FAILED" << std::endl;
       return false;
    } catch( ObserverAlreadyKnownToSubject& e ){
      return true;
    }
}

bool testAddingSameObserverMethod(){
    PRINT_TEST_RUN("testAddingSameObserverMethod");
    
    ObsTestIntTwo obi1;
    SubTestInt sub,sub2;

    try{
       sub.addObserver(obi1);
       sub.addObserver(obi1);
       std::cout << std::endl << "Added same Observer ... FAILED" << std::endl;
       return false;
    } catch( ObserverAlreadyKnownToSubject& e ){
      return true;
    }
}

bool testRemovingObserverMethod(){
    PRINT_TEST_RUN("testRemovingObserverMethod");
    
    ObsTestIntTwo obi1;
    SubTestInt sub;

    try{
        sub += obi1;
        sub.removeObserver(obi1);
        sub += obi1;

        return true;
    } catch( ... ){
         std::cout << std::endl << " Should not throw exception" << std::endl;
         return false;
    }
}

bool testRemovingObserverOperator(){
    PRINT_TEST_RUN("testRemovingObserverOperator");
    
    ObsTestIntTwo obi1;
    SubTestInt sub;

    try{
        ((sub += obi1) -= obi1) += obi1;
        return true;
    } catch( ... ){
         std::cout << std::endl << " Should not throw exception" << std::endl;
         return false;
    }
}

bool testRemovingFromEmptyContainer(){
    PRINT_TEST_RUN("testRemovingFromEmptyContainer");
    
    ObsTestIntTwo obi1;
    SubTestInt sub;

    try{
       sub -= obi1;
       std::cout << std::endl << "Removed Non Existent Observer" << std::endl;
       return false;
    } catch( ObserverUnknownToSubject& e ){
      return true;
    } catch(...){
        std::cout << std::endl << "Thrown bad Exception ... FAILED" << std::endl;
        return false;
    }
}

bool testUsingNotifyToAllObserversMethod(){
    PRINT_TEST_RUN("testUsingNotifyToAllObserversMethod");
    
    ObsTestIntTwo obi1,obi2,obi3;
    SubTestInt sub;

    ((sub += obi1) += obi2) += obi3;
    sub.notify(5);

    return getEqualTestResult(3,test);
}


bool testUsingNotifyToAllObserversOperator(){
  PRINT_TEST_RUN("testUsingNotifyToAllObserversMethod");
    
    ObsTestIntTwo obi1,obi2,obi3;
  SubTestInt sub;

  ((sub += obi1) += obi2) += obi3;
  sub(42)(32)(22)(11);

  return getEqualTestResult(15,test);
}


bool testNotifyAfterRemoving(){
    PRINT_TEST_RUN("testUsingNotifyToAllObserversMethod");
    ObsTestIntTwo obi1,obi2,obi3;
    SubTestInt sub;

    ((((sub += obi1) += obi2) += obi3) -= obi3) -= obi1;
    sub(42);

    return getEqualTestResult(16,test);
}


bool testNotifyBeforeAdding(){
    PRINT_TEST_RUN("testUsingNotifyToAllObserversMethod");
    ObsTestIntTwo obi1,obi2,obi3;
    SubTestInt sub;

    sub(42);
    ((sub += obi1) += obi2) += obi3;

    return getEqualTestResult(16,test);
}

bool testNotifyTwoDifferentSubjects(){
    PRINT_TEST_RUN("testUsingNotifyToAllObserversMethod");
    ObsTestIntTwo obi1,obi2,obi3;
    SubTestInt sub,sub2;

    ((sub += obi1) += obi2) += obi3;
    sub2 += obi3;
    sub2(324);

    return getEqualTestResult(17,test);
}




int subject_test(){
  std::cout << "Running PartTwoObserverTests" << std::endl;
  TestClass* t = new TestClass();

  t->runTest(testAddingAnObserver());
  t->runTest(testAddingSameObserverOperator());
  t->runTest(testAddingSameObserverMethod());
  t->runTest(testRemovingFromEmptyContainer());
  t->runTest(testRemovingObserverMethod());
  t->runTest(testRemovingObserverOperator());
  t->runTest(testUsingNotifyToAllObserversMethod());
  t->runTest(testUsingNotifyToAllObserversOperator());
  t->runTest(testNotifyAfterRemoving());
  t->runTest(testNotifyBeforeAdding());
  t->runTest(testNotifyTwoDifferentSubjects());


  std::cout << *t;
  delete t;
}


#endif
