#ifndef OOP5_SERG_UTIL_TEST
#define OOP5_SERG_UTIL_TEST

#include ".\..\..\part1\Utilities.h"
#include ".\..\BaseTestFile.h"
#include <iostream>



bool basicINTCreationTest(){
  PRINT_TEST_RUN("basicINTCreationTest");
  typedef Int<5> wrappedFive;
  int test = wrappedFive::value;
  return getEqualTestResult(5,test);
}


bool basicLISTCreationSIZETest(){
  PRINT_TEST_RUN("basicLISTCreationSIZETest");
  typedef List<Int<4>,Int<3>,Int<2>> List432;
  int size = List432::size;
  return getEqualTestResult(3,size);
}

bool basicLISTCreationHeadTest(){
  PRINT_TEST_RUN("basicLISTCreationHeadTest");
  typedef List<Int<4>,Int<3>,Int<2>> List432;
  int value = List432::head::value;
  return getEqualTestResult(4,value);
}

bool basicLISTCreationNextTest(){
  PRINT_TEST_RUN("basicLISTCreationNextTest");
  typedef List<Int<4>,Int<3>,Int<2>> List432;
  int value = List432::next::next::head::value;
  return getEqualTestResult(2,value);
}


bool emptyListCreationSize(){
  PRINT_TEST_RUN("emptyListCreationSize");
  typedef List<> emptyList;
  int size = emptyList::size;
  return getEqualTestResult(0,size);
}

bool singleLinkedListHead(){
  PRINT_TEST_RUN("singleLinkedListHead");
  typedef List<Int<1000>> singleLink;
  int size = singleLink::size;
  int value = singleLink::head::value;
  return getEqualTestResult(1,size) && getEqualTestResult(1000,value);
}


bool testPrependListCreation(){
  PRINT_TEST_RUN("testPrependListCreation");
  typedef List<Int<1>, Int<2>, Int<3> > list;
  typedef typename PrependList<Int<4>, list>::list newList;
  int value = newList::head::value;
  return getEqualTestResult(4,value);
}

bool testPrependListCreationVarFromOriginal(){
  PRINT_TEST_RUN("testPrependListCreationVarFromOriginal");
  typedef List<Int<1> > list;
  typedef typename PrependList<Int<4>, list>::list newList;
  int value = newList::next::head::value;
  return getEqualTestResult(1,value);
}

bool testMultiplePrepends(){
   PRINT_TEST_RUN("testMultiplePrepends");
   typedef List<Int<1> > list;
   typedef typename PrependList<Int<4>, list>::list newList;
   typedef typename PrependList<Int<5>, newList>::list secondList;
   int first_value = secondList::head::value;
   int second_value = secondList::next::head::value;
   int orig_value =  secondList::next::next::head::value;

   return (getEqualTestResult(5,first_value) &&
    getEqualTestResult(4,second_value) &&
    getEqualTestResult(1,orig_value));
}


bool testListGet(){
    PRINT_TEST_RUN("testListGet");
    typedef List<Int<1>, Int<2>, Int<3>> list;
    typedef ListGet<1, list>::value second;
    int second_value = second::value;

    return getEqualTestResult(2,second_value);
}


bool testListGetWithPrepend(){
    PRINT_TEST_RUN("testListGetWithPrepend");
    typedef List<Int<1>, Int<2>, Int<3>> list;
    typedef typename PrependList<Int<4>, list>::list newList;
    typedef ListGet<1, newList>::value second;
    int second_value = second::value;

    return getEqualTestResult(1,second_value);
}

bool testListSet(){
    PRINT_TEST_RUN("testListSet");
    typedef List<Int<1>, Int<2>, Int<3>> list;
    typedef typename ListSet<1, Int<5>, list>::list listA;
    typedef typename ListSet<0, Int<20>, listA>::list listB;
    int first_value = listB::head::value;
    int second_value = listB::next::head::value;

    return (getEqualTestResult(20,first_value)
      && getEqualTestResult(5,second_value));
}

bool testAllCombinations(){
  PRINT_TEST_RUN("testAllCombinations");
  typedef List<Int<1>, Int<2>, Int<3>> list;
  typedef typename ListSet<1, Int<4>, list>::list listA;
  typedef typename ListSet<0, Int<13>, listA>::list listB;
  typedef typename PrependList<Int<27>, listB>::list listC;
  int first_value = ListGet<0, listC>::value::value;
  int second_value = ListGet<1, listC>::value::value;
  int third_value = ListGet<2, listC>::value::value;
  int fourth_value = ListGet<3, listC>::value::value;

  int sum = first_value + second_value + third_value + fourth_value;

  return (getEqualTestResult(47,sum));
}




int utility_test() {
  std::cout << "Running PartOneUtilityTests" << std::endl;
  TestClass* t = new TestClass();
  t->runTest(basicINTCreationTest());
  t->runTest(basicLISTCreationSIZETest());
  t->runTest(basicLISTCreationHeadTest());
  t->runTest(basicLISTCreationNextTest());
  t->runTest(emptyListCreationSize());
  t->runTest(singleLinkedListHead());
  t->runTest(testPrependListCreation());
  t->runTest(testPrependListCreationVarFromOriginal());
  t->runTest(testMultiplePrepends());
  t->runTest(testListGet());
  t->runTest(testListGetWithPrepend());
  t->runTest(testListSet());
  t->runTest(testAllCombinations());

  std::cout << *t;
  delete t;
}

#endif
