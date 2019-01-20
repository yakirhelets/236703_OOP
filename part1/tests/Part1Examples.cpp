#include <iostream>
#include "MatrixOperations.h"

#define LIST "ListGet"
#define PREPEND_LIST "PrependList"
#define LIST_GET "ListGet"
#define LIST_SET "ListSet"
#define SUM_LISTS "SumLists"
#define SUM_OF_LIST "SumOfList"
#define MULTI_LISTS "MultiLists"
#define MULTI_MATRIX "MultiMatrix"

#define ADD_MATRIX "AddMatrix"

int main() {

    // List
    typedef List<Int<1>, Int<2>, Int<3>> list1;
    static_assert(list1::head::value == 1, LIST); // = Int<1>
    static_assert(list1::size == 3, "Failed"); // = 3

    typedef typename list1::next list1Tail; // = List<Int<2>, Int<3>>
    static_assert(list1Tail::head::value == 2, LIST);
    static_assert(list1Tail::size == 2, "Failed"); // = 2

    typedef typename list1Tail::next list2Tail; // = List<Int<3>>
    static_assert(list2Tail::head::value == 3, LIST);
    static_assert(list2Tail::size == 1, LIST);

    typedef typename list2Tail::next emptyList; // = List<>
    static_assert(emptyList::size == 0, LIST);

    // Prepend List
    typedef List<Int<1>, Int<2>, Int<3>> list2;
    typedef typename PrependList<Int<4>, list2>::list prependList; // = List< Int<4>, Int<1>, Int<2>, Int<3>>

    static_assert(prependList::head::value == 4, PREPEND_LIST);
    static_assert(prependList::size == 4, PREPEND_LIST);

    typedef typename prependList::next prependList2;
    static_assert(prependList2::head::value == 1, PREPEND_LIST);
    static_assert(prependList2::size == 3, PREPEND_LIST);

    typedef typename prependList2::next prependList3;
    static_assert(prependList3::head::value == 2, PREPEND_LIST);
    static_assert(prependList3::size == 2, PREPEND_LIST);

    typedef typename prependList3::next prependList4;
    static_assert(prependList4::head::value == 3, PREPEND_LIST);
    static_assert(prependList4::size == 1, PREPEND_LIST);

    typedef typename prependList4::next prependList5;
    static_assert(prependList5::size == 0, PREPEND_LIST);

    // ListGet
    typedef List<Int<1>, Int<2>, Int<3>> list3;
    static_assert(ListGet<0, list3>::value::value == 1, LIST_GET); // = Int<1>
    static_assert(ListGet<1, list3>::value::value == 2, LIST_GET); // = Int<2>
    static_assert(ListGet<2, list3>::value::value == 3, LIST_GET); // = Int<3>

    // ListSet
    typedef List<Int<1>, Int<2>, Int<3>> list4;
    typedef typename ListSet<0, Int<5>, list4>::list listA; // = List<Int<5>, Int<2>, Int<3>>
    typedef typename ListSet<0, Int<5>, List<Int<1>, Int<2>, Int<3>>>::list listA; // = List<Int<5>, Int<2>, Int<3>>
    static_assert(ListGet<0, listA>::value::value == 5, LIST_SET);
    static_assert(ListGet<1, listA>::value::value == 2, LIST_SET);
    static_assert(ListGet<2, listA>::value::value == 3, LIST_SET);
    static_assert(listA::size == 3, LIST_SET);

    typedef typename ListSet<1, Int<5>, listA>::list listB;
    static_assert(ListGet<0, listB>::value::value == 5, LIST_SET);
    static_assert(ListGet<1, listB>::value::value == 5, LIST_SET);
    static_assert(ListGet<2, listB>::value::value == 3, LIST_SET);
    static_assert(listB::size == 3, LIST_SET);

    typedef typename ListSet<2, Int<5>, listB>::list listC;
    static_assert(ListGet<0, listC>::value::value == 5, LIST_SET);
    static_assert(ListGet<1, listC>::value::value == 5, LIST_SET);
    static_assert(ListGet<2, listC>::value::value == 5, LIST_SET);
    static_assert(listB::size == 3, LIST_SET);

    // ************ Matrix Operations ************

    typedef List<
            List<Int<1>, Int<2>, Int<3> >,
            List<Int<4>, Int<5>, Int<6> >,
            List<Int<7>, Int<8>, Int<9> >
    > matrix11;

    typedef List<
            List<Int<9>, Int<8>, Int<7> >,
            List<Int<6>, Int<5>, Int<4> >,
            List<Int<3>, Int<2>, Int<1> >
    > matrix21;

    // = List<
    //			List< Int<10>, Int<10>, Int<10> >,
    //			List< Int<10>, Int<10>, Int<10> >,
    //			List< Int<10>, Int<10>, Int<10> >
    //		 >

    typedef typename Add<matrix11, matrix21>::result matrix31;

    static_assert(ListGet<0, ListGet<0, matrix31>::value>::value::value == 10, MULTI_LISTS);
    static_assert(ListGet<1, ListGet<0, matrix31>::value>::value::value == 10, MULTI_LISTS);
    static_assert(ListGet<2, ListGet<0, matrix31>::value>::value::value == 10, MULTI_LISTS);

    static_assert(ListGet<0, ListGet<1, matrix31>::value>::value::value == 10, MULTI_LISTS);
    static_assert(ListGet<1, ListGet<1, matrix31>::value>::value::value == 10, MULTI_LISTS);
    static_assert(ListGet<2, ListGet<1, matrix31>::value>::value::value == 10, MULTI_LISTS);

    static_assert(ListGet<0, ListGet<2, matrix31>::value>::value::value == 10, MULTI_LISTS);
    static_assert(ListGet<1, ListGet<2, matrix31>::value>::value::value == 10, MULTI_LISTS);
    static_assert(ListGet<2, ListGet<2, matrix31>::value>::value::value == 10, MULTI_LISTS);

    typedef List<
            List<Int<410>, Int<40>, Int<38> >,
            List<Int<37>, Int<0>, Int<10> >,
            List<Int<41>, Int<32>, Int<300> >
    > matrix42;

    typedef List<
            List<Int<10>, Int<2>, Int<4> >,
            List<Int<5>, Int<420>, Int<32> >,
            List<Int<1>, Int<10>, Int<120> >
    > matrix24;

    // = List<
    //			List< Int<420>, Int<42>, Int<42> >,
    //			List< Int<42>, Int<420>, Int<42> >,
    //			List< Int<42>, Int<42>, Int<420> >
    //		 >

    typedef typename Add<matrix42, matrix24>::result matrix420;

    static_assert(ListGet<0, ListGet<0, matrix420>::value>::value::value == 420, ADD_MATRIX);
    static_assert(ListGet<1, ListGet<0, matrix420>::value>::value::value == 42, ADD_MATRIX);
    static_assert(ListGet<2, ListGet<0, matrix420>::value>::value::value == 42, ADD_MATRIX);

    static_assert(ListGet<0, ListGet<1, matrix420>::value>::value::value == 42, ADD_MATRIX);
    static_assert(ListGet<1, ListGet<1, matrix420>::value>::value::value == 420, ADD_MATRIX);
    static_assert(ListGet<2, ListGet<1, matrix420>::value>::value::value == 42, ADD_MATRIX);

    static_assert(ListGet<0, ListGet<2, matrix420>::value>::value::value == 42, ADD_MATRIX);
    static_assert(ListGet<1, ListGet<2, matrix420>::value>::value::value == 42, ADD_MATRIX);
    static_assert(ListGet<2, ListGet<2, matrix420>::value>::value::value == 420, ADD_MATRIX);

    // ***** CHECK THAT DOESN'T COMPILE *******
//    typedef List<
//            List<Int<410>, Int<40>, Int<38> >,
//            List<Int<37>, Int<0>, Int<10> >,
//            List<Int<41>, Int<32>, Int<300> >
//    > m1AddBad;
//
//    typedef List<
//            List<Int<10>, Int<2>, Int<4> >,
//            List<Int<5>, Int<420>, Int<32> >
//    > m2AddBad;
//
//    typedef typename Add<m1AddBad, m2AddBad>::result matrixBadSize; // Doesn't Compile!


    // Multiply
    typedef List<
            List<Int<1>, Int<2> >,
            List<Int<0>, Int<1> >
    > matrix12;

    typedef List<
            List<Int<0>, Int<7> >,
            List<Int<8>, Int<0> >
    > matrix22;

    typedef typename Multiply<matrix12, matrix22>::result matrix32;
    // = List<
    //			List< Int<16>, Int<7> >,
    //			List< Int<8>, Int<0> >
    //		  >

    static_assert(ListGet<0, ListGet<0, matrix32>::value>::value::value == 16, MULTI_MATRIX);
    static_assert(ListGet<1, ListGet<0, matrix32>::value>::value::value == 7, MULTI_MATRIX);

    static_assert(ListGet<0, ListGet<1, matrix32>::value>::value::value == 8, MULTI_MATRIX);
    static_assert(ListGet<1, ListGet<1, matrix32>::value>::value::value == 0, MULTI_MATRIX);

    // **** CHECK THAT DOESN'T COMPILE *****
//    // 3x3
//    typedef List<
//            List<Int<410>, Int<40>, Int<38> >,
//            List<Int<37>, Int<0>, Int<10> >,
//            List<Int<41>, Int<32>, Int<300> >
//    > m1;
//
//    // 2x1
//    typedef List<
//            List<Int<0>>,
//            List<Int<8>>
//    > m2;
//
//    typedef typename Multiply<m1, m2>::result multi; // Doesn't COMPILE

    typedef List<
            List<Int<1>, Int<2>, Int<0>>,
            List<Int<0>, Int<1>, Int<0>>,
            List<Int<1>, Int<0>, Int<5>>
    > multi1;

    typedef List<
            List<Int<1>, Int<2>>,
            List<Int<0>, Int<1>>,
            List<Int<1>, Int<0>>
    > multi2;

//    List<
//            List<Int<1>, Int<4>>,
//            List<Int<0>, Int<1>>,
//            List<Int<6>, Int<2>>
//    >

    typedef typename Multiply<multi1, multi2>::result multiCheck;

    static_assert(ListGet<0, ListGet<0, multiCheck>::value>::value::value == 1, MULTI_MATRIX);
    static_assert(ListGet<1, ListGet<0, multiCheck>::value>::value::value == 4, MULTI_MATRIX);

    static_assert(ListGet<0, ListGet<1, multiCheck>::value>::value::value == 0, MULTI_MATRIX);
    static_assert(ListGet<1, ListGet<1, multiCheck>::value>::value::value == 1, MULTI_MATRIX);

    static_assert(ListGet<0, ListGet<2, multiCheck>::value>::value::value == 6, MULTI_MATRIX);
    static_assert(ListGet<1, ListGet<2, multiCheck>::value>::value::value == 2, MULTI_MATRIX);


    static_assert(multiCheck::size == 3, MULTI_MATRIX); // height
    static_assert(ListGet<0, multiCheck>::value::size == 2, MULTI_MATRIX); // width

    std::cout << "Passed" << std::endl;
}