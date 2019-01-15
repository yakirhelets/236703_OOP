#ifndef OOP5_SERG_TEST_TWO
#define OOP5_SERG_TEST_TWO


#include ".\..\..\part1\MatrixOperations.h"
#include ".\..\BaseTestFile.h"
#include <iostream>


bool basicMatrixCreationTest(){
  PRINT_TEST_RUN("basicMatrixCreationTest");
  typedef List<
 List< Int<1>, Int<2>>,
 List< Int<4>, Int<5>>,
 List< Int<1>, Int<2>>
 > matrix1;
 int row_size = matrix1::size;
 int col_size = matrix1::head::size;

 return (getEqualTestResult(2, col_size) && getEqualTestResult(3, row_size));
}
bool testOneRowAddition(){
  PRINT_TEST_RUN("testOneRowAddition");
  typedef List<
 List< Int<1>, Int<2>, Int<0> >
 > matrix1;
 typedef List<
 List< Int<1>, Int<2>, Int<0> >
  > matrix2;
  typedef typename Add<matrix1, matrix2>::result matrix3;
  int secondVecValue = ListGet<1, typename ListGet<0, matrix3>::value >::value::value;
  return getEqualTestResult(4, secondVecValue);
}


bool testOneColAddition(){
  PRINT_TEST_RUN("testOneColAddition");
  typedef List<
 List< Int<1> >,
 List< Int<2> >,
 List< Int<3> >,
 List< Int<4> >
 > matrix1;
 typedef List<
 List< Int<1> >,
 List< Int<2> >,
 List< Int<3> >,
 List< Int<4> >
  > matrix2;
  typedef typename Add<matrix1, matrix2>::result matrix3;
  int fourth_value = ListGet<0, typename ListGet<3, matrix3>::value >::value::value;
  return getEqualTestResult(8, fourth_value);
}


bool testMultipleRowAddition(){
  PRINT_TEST_RUN("testMultipleRowAddition");
  typedef List<
 List< Int<1>, Int<2>, Int<0> >,
 List< Int<4>, Int<5>, Int<3> >,
 List< Int<1>, Int<2>, Int<6> >
 > matrix1;
 typedef List<
 List< Int<1>, Int<2>, Int<0> >,
 List< Int<4>, Int<5>, Int<3> >,
 List< Int<1>, Int<2>, Int<6> >
  > matrix2;
  typedef typename Add<matrix1, matrix2>::result matrix3;
  int MiddleValue = ListGet<1, typename ListGet<1, matrix3>::value>::value::value;
  return getEqualTestResult(10, MiddleValue);
}

bool testMatrixMultScalar(){
  PRINT_TEST_RUN("testMatrixMultScalar");
  typedef List<
 List< Int<6> >
 > matrix1;
 typedef List<
 List< Int<2>>
  > matrix2;
  typedef typename Multiply<matrix1, matrix2>::result matrix32;
  int first_value = MatrixGet<0,0,matrix32>::value::value;
  return getEqualTestResult(12, first_value);
}


bool testMatrixMultIdentity(){
  PRINT_TEST_RUN("testMatrixMultIdentity");
  typedef List<
  List< Int<5>, Int<5>, Int<5> >,
  List< Int<5>, Int<5>, Int<5> >,
  List< Int<5>, Int<5>, Int<5> >
 > matrix1;
 typedef List<
 List< Int<1>, Int<0>, Int<0> >,
 List< Int<0>, Int<1>, Int<0> >,
 List< Int<0>, Int<0>, Int<1> >
  > matrix2;
  typedef typename Multiply<matrix1, matrix2>::result matrix32;
  int test_values[9];


  //test_values[0] = MatrixGet<0,0,matrix32>::value::value;
  test_values[0] = ListGet<0, typename ListGet<0, matrix32>::value>::value::value;
  // test_values[1] = MatrixGet<0,1,matrix32>::value::value;
  test_values[1] = ListGet<1, typename ListGet<0, matrix32>::value >::value::value;
  // test_values[2] = MatrixGet<0,2,matrix32>::value::value;
  test_values[2] = ListGet<2, typename ListGet<0, matrix32>::value >::value::value;

  // test_values[3] = MatrixGet<1,0,matrix32>::value::value;
  test_values[3] = ListGet<0, typename ListGet<1, matrix32>::value >::value::value;
  // test_values[4] = MatrixGet<1,1,matrix32>::value::value;
  test_values[4] = ListGet<1, typename ListGet<1, matrix32>::value >::value::value;
  // test_values[5] = MatrixGet<1,2,matrix32>::value::value;
  test_values[5] = ListGet<2, typename ListGet<1, matrix32>::value >::value::value;

  // test_values[6] = MatrixGet<2,0,matrix32>::value::value;
  test_values[6] = ListGet<0, typename ListGet<2, matrix32>::value >::value::value;
  // test_values[7] = MatrixGet<2,1,matrix32>::value::value;
  test_values[7] = ListGet<1, typename ListGet<2, matrix32>::value >::value::value;
  // test_values[8] = MatrixGet<2,2,matrix32>::value::value;
  test_values[8] = ListGet<2, typename ListGet<2, matrix32>::value >::value::value;

  bool result = true;
  int iter = 0;
  while( iter < 9){
      result = result && getEqualTestResult(5,test_values[iter]);
      iter++;
  }
  return result;
}



bool testMatrixMultiplication(){
  PRINT_TEST_RUN("testMatrixMultiplication");
  typedef List<
  List< Int<5>, Int<-2>, Int<4> >,
  List< Int<3>, Int<2>, Int<3> >
 > matrix1;
 typedef List<
 List< Int<8>, Int<3> >,
 List< Int<4>, Int<1> >,
 List< Int<5>, Int<-2> >
  > matrix2;
  typedef typename Multiply<matrix1, matrix2>::result matrix32;
  int test_values[4];


  //test_values[0] = MatrixGet<0,0,matrix32>::value::value;
  test_values[0] = ListGet<0, typename ListGet<0, matrix32>::value>::value::value;
  // test_values[1] = MatrixGet<0,1,matrix32>::value::value;
  test_values[1] = ListGet<1, typename ListGet<0, matrix32>::value >::value::value;

  // test_values[2] = MatrixGet<1,0,matrix32>::value::value;
  test_values[2] = ListGet<0, typename ListGet<1, matrix32>::value >::value::value;
  // test_values[3] = MatrixGet<1,1,matrix32>::value::value;
  test_values[3] = ListGet<1, typename ListGet<1, matrix32>::value >::value::value;


  int actual_values[4];
  actual_values[0] = 52;
  actual_values[1] = 5;
  actual_values[2] = 47;
  actual_values[3] = 5;


  bool result = true;
  int iter = 0;
  while( iter < 4){
      result = result && getEqualTestResult(actual_values[iter],test_values[iter]);
      iter++;
  }
  return result;
}


bool testMatrixMultiplicationByZero(){
  PRINT_TEST_RUN("testMatrixMultiplicationByZero");
  typedef List<
  List< Int<1>, Int<2>, Int<3> >,
  List< Int<1>, Int<2>, Int<3> >,
  List< Int<1>, Int<2>, Int<3> >
 > matrix1;
 typedef List<
 List< Int<0>, Int<0>, Int<0> >,
 List< Int<0>, Int<0>, Int<0> >,
 List< Int<0>, Int<0>, Int<0> >
  > matrix2;
  typedef typename Multiply<matrix1, matrix2>::result matrix32;
  int test_values[9];

  //test_values[0] = MatrixGet<0,0,matrix32>::value::value;
  test_values[0] = ListGet<0, typename ListGet<0, matrix32>::value>::value::value;
  // test_values[1] = MatrixGet<0,1,matrix32>::value::value;
  test_values[1] = ListGet<1, typename ListGet<0, matrix32>::value >::value::value;
  // test_values[2] = MatrixGet<0,2,matrix32>::value::value;
  test_values[2] = ListGet<2, typename ListGet<0, matrix32>::value >::value::value;

  // test_values[3] = MatrixGet<1,0,matrix32>::value::value;
  test_values[3] = ListGet<0, typename ListGet<1, matrix32>::value >::value::value;
  // test_values[4] = MatrixGet<1,1,matrix32>::value::value;
  test_values[4] = ListGet<1, typename ListGet<1, matrix32>::value >::value::value;
  // test_values[5] = MatrixGet<1,2,matrix32>::value::value;
  test_values[5] = ListGet<2, typename ListGet<1, matrix32>::value >::value::value;

  // test_values[6] = MatrixGet<2,0,matrix32>::value::value;
  test_values[6] = ListGet<0, typename ListGet<2, matrix32>::value >::value::value;
  // test_values[7] = MatrixGet<2,1,matrix32>::value::value;
  test_values[7] = ListGet<1, typename ListGet<2, matrix32>::value >::value::value;
  // test_values[8] = MatrixGet<2,2,matrix32>::value::value;
  test_values[8] = ListGet<2, typename ListGet<2, matrix32>::value >::value::value;

  bool result = true;
  int iter = 0;
  while( iter < 9){
      result = result && getEqualTestResult(0,test_values[iter]);
      iter++;
  }
  return result;
}





// bool testEmptyRowMatrix(){
//   PRINT_TEST_RUN("testEmptyRowMatrix");
//   typedef List<
//    List<>,
//    List<>,
//    List<>
//  > matrix1;
//  typedef List<
//     List<>,
//     List<>,
//     List<>
//   > matrix2;
//   typedef typename Add<matrix1, matrix2>::result matrix3;
//   int matrix_size = matrix3::size;
//   return getEqualTestResult(3, matrix_size);
// }
//
// bool testEmptyMatrix(){
//   PRINT_TEST_RUN("testEmptyMatrix");
//   typedef List<
//  > matrix1;
//  typedef List<
//   > matrix2;
//   typedef typename Add<matrix1, matrix2>::result matrix3;
//   int matrix_size = matrix3::size;
//   return getEqualTestResult(0, matrix_size);
// }


int matrix_test(){
  std::cout << "Running PartOneMatrixTests" << std::endl;
  TestClass* t = new TestClass();

  t->runTest(basicMatrixCreationTest());
  t->runTest(testOneRowAddition());
  t->runTest(testOneColAddition());
  t->runTest(testMultipleRowAddition());
  // t->runTest(testEmptyRowMatrix());
  // t->runTest(testEmptyMatrix());

  //Implement Get To Use
  t->runTest(testMatrixMultScalar());
  t->runTest(testMatrixMultIdentity());
  t->runTest(testMatrixMultiplication());
  t->runTest(testMatrixMultiplicationByZero());

  std::cout << *t;
  delete t;
}


#endif
