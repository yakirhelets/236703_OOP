#ifndef TEST_CLASS
#define TEST_CLASS

#include <iostream>
using namespace std;
// PRINTS
#define PRINT_TEST_RUN(testName) (std::cout << "Running " << testName << "...")
#define PRINT_SUCCESS() (std::cout << "SUCCESS" << std::endl)
#define PRINT_FAILED() (std::cout << "FAILED" << std::endl)
#define PRINT_RESULT(first, second) (std::cout << std::endl <<\
                                      "Expected: " << first\
                                      << " ,Actual: "<< second << std::endl)

//Testing
#define EQUALS(first, second) ((first) == (second) ? true : false )
#define DIFF(first, second) ((first) == (second) ? false : true)




class TestClass{
private:
  int suc_tests;
  int failed_tests;
public:
  TestClass(): suc_tests(0), failed_tests(0){}
    void runTest(bool testResult) {
    if(testResult){
      PRINT_SUCCESS();
      suc_tests++;
    } else{
      PRINT_FAILED();
      failed_tests++;
    }
  }
  int getSuccess(){
    return suc_tests;
  }
  int getFailed(){
    return failed_tests;
  }
};

template<typename T>
bool getEqualTestResult(const T& first,const T& second){
  bool test_result = EQUALS(first,second);
  if(test_result){
    return true;
  } else {
    PRINT_RESULT(first,second);
    return false;
  }
}

std::ostream& operator<<(std::ostream &out,TestClass& data){
     out << std::endl;
     out << "The number of tests passed: " << data.getSuccess() << std::endl;
     out << "The number of tests failed: " << data.getFailed() << std::endl;
     out << std::endl;

     return out;
  };

#endif
