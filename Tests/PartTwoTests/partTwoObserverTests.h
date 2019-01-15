#ifndef OOP5_SERG_TEST_THREE
#define OOP5_SERG_TEST_THREE

#include ".\..\BaseTestFile.h"
#include ".\..\..\part2\Observer.h"

class ObsTestInt : public Observer<int> {
private:
     int test;
public:
    ObsTestInt() : Observer(){}
    int getTest() { return test; }
    void handleEvent(const int& msg) override {
       test = msg;
    }
};


class ObsTestString : Observer<std::string> {
private:
     std::string test;
public:
    ObsTestString() : Observer(){}
    std::string getTest() { return test; }
    void handleEvent(const string& msg) override {
       test = msg;
    }
};

bool testBasicObserverCreationINT() {
    PRINT_TEST_RUN("testBasicObserverCreationINT");

    ObsTestInt obi;
    obi.handleEvent(4);

    bool result = getEqualTestResult(4,obi.getTest());

    obi.handleEvent(-2);

    result = result && getEqualTestResult(-2,obi.getTest());

    return result;
}

int observer_test(){
  std::cout << "Running PartTwoObserverTests" << std::endl;
  TestClass* t = new TestClass();

  t->runTest((testBasicObserverCreationINT()));



  std::cout << *t;
  delete t;
}

#endif
