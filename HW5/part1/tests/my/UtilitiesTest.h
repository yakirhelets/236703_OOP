#ifndef OOP5_UTILITIESTEST_H
#define OOP5_UTILITIESTEST_H

#include "TestUtilities.h"

class UtilitiesTest {
private:
    void listHead() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        static_assert(list::head::value == Int<0>::value, "error"); // NOLINT
    }

    void listNext() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        static_assert(list::next::head::value == Int<1>::value, "error"); // NOLINT
    }

    void listNext_size1() {
        typedef List<Int<0>> list;
        static_assert(list::next::size == 0, "error");
    }

    void listSize_emtpy() {
        typedef List<> list;
        static_assert(list::size == 0, "error");
    }

    void listSize_nonEmtpy() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        static_assert(list::size == 3, "error");
    }

    void list_staff() {
        typedef List<Int<1>, Int<2>, Int<3>> list;
        AssertInt<list::head, Int<1>> assertHead;
        typedef typename list::next listTail;
        AssertList<listTail, List<Int<2>, Int<3>>> assertTail;
        static_assert(list::size == 3, "error");
    }

    void prependList_emtpy() {
        typedef List<> list;
        typedef typename PrependList<Int<0>, list>::list prependList;
        static_assert(prependList::size == 1, "error");
        static_assert(prependList::head::value == 0, "error");
    }

    void prependList_nonEmtpy() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        typedef typename PrependList<Int<-1>, list>::list prependList;
        static_assert(prependList::size == 4, "error");
        static_assert(prependList::head::value == -1, "error");
    }

    void prependList_staff() {
        typedef List<Int<1>, Int<2>, Int<3>> list;
        typedef typename PrependList<Int<4>, list>::list newList;
        AssertList<
                newList,
                List<Int<4>, Int<1>, Int<2>, Int<3>>
        > assertNewList;
    }

    void listGet_size1() {
        typedef List<Int<0>> list;
        static_assert(list::size == 1, "error");
        static_assert(ListGet<0, list>::value::value == 0, "error");
    }

    void listGet_size3() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        static_assert(list::size == 3, "error");
        static_assert(ListGet<0, list>::value::value == 0, "error");
        static_assert(ListGet<1, list>::value::value == 1, "error");
        static_assert(ListGet<2, list>::value::value == 2, "error");
    }

    void listGet_staff() {
        typedef List<Int<1>, Int<2>, Int<3>> list;
        AssertInt<
                ListGet<0, list>::value,
                Int<1>
        > assertValue0;
        AssertInt<
                ListGet<2, list>::value,
                Int<3>
        > assertValue2;
    }

    void listSet_0() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        typedef ListSet<0, Int<5>, list>::list newList;
        static_assert(newList::size == 3, "error");
        static_assert(ListGet<0, newList>::value::value == 5, "error");
        static_assert(ListGet<1, newList>::value::value == 1, "error");
        static_assert(ListGet<2, newList>::value::value == 2, "error");
    }

    void listSet_1() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        typedef ListSet<1, Int<5>, list>::list newList;
        static_assert(newList::size == 3, "error");
        static_assert(ListGet<0, newList>::value::value == 0, "error");
        static_assert(ListGet<1, newList>::value::value == 5, "error");
        static_assert(ListGet<2, newList>::value::value == 2, "error");
    }

    void listSet_2() {
        typedef List<Int<0>, Int<1>, Int<2>> list;
        typedef ListSet<2, Int<5>, list>::list newList;
        static_assert(newList::size == 3, "error");
        static_assert(ListGet<0, newList>::value::value == 0, "error");
        static_assert(ListGet<1, newList>::value::value == 1, "error");
        static_assert(ListGet<2, newList>::value::value == 5, "error");
    }

    void listSet_size1() {
        typedef List<Int<0>> list;
        typedef ListSet<0, Int<5>, list>::list newList;
        static_assert(newList::size == 1, "error");
        static_assert(ListGet<0, newList>::value::value == 5, "error");
    }

    void listSet_staff() {
        typedef List<Int<1>, Int<2>, Int<3>> list;
        typedef typename ListSet<0, Int<5>, list>::list listA;
        AssertList<
                listA,
                List<Int<5>, Int<2>, Int<3>>
        > assertListA;
        typedef typename ListSet<2, Int<7>, list>::list listB;
        AssertList<
                listB,
                List<Int<1>, Int<2>, Int<7>>
        > assertListB;
    }

    void int_staff() {
        typedef Int<5> wrappedFive;
        static_assert(wrappedFive::value == 5, "error");
    }
};

#endif //OOP5_UTILITIESTEST_H
