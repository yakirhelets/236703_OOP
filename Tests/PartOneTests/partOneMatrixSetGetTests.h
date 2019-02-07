#ifndef OOP5_SERG_TEST_ONE
#define OOP5_SERG_TEST_ONE


#include ".\..\..\part1\MatrixOperations.h"
#include ".\..\BaseTestFile.h"
#include <iostream>


bool testMatrixGet(){
  PRINT_TEST_RUN("testMatrixGet");
  typedef List<
 List< Int<1>, Int<2>, Int<0> >,
 List< Int<4>, Int<5>, Int<3> >,
 List< Int<1>, Int<2>, Int<6> >
 > matrix1;
  int pre_final_value = MatrixGet<2,1,matrix1>::value::value;
  int first_value = MatrixGet<0,0,matrix1>::value::value;
  return (getEqualTestResult(2,pre_final_value) && getEqualTestResult(1,first_value));
}


bool testMatrixSet(){
  PRINT_TEST_RUN("testMatrixSet");
  typedef List<
 List< Int<1>, Int<2>, Int<0> >,
 List< Int<4>, Int<5>, Int<3> >,
 List< Int<1>, Int<2>, Int<6> >
 > matrix1;
  typedef MatrixSet<0,0,Int<150>,matrix1>::list mod_matrix1;
  int first_value = MatrixGet<0,0,mod_matrix1>::value::value;
  return (getEqualTestResult(150,first_value));
}




int get_set_matrix_test(){
  std::cout << "Running PartOneMatrixTests" << std::endl;
  TestClass* t = new TestClass();

  t->runTest(testMatrixGet());
  t->runTest(testMatrixSet());

  std::cout << *t;
  delete t;
}

#endif
