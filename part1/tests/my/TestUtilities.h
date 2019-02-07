#ifndef OOP5_TESTUTILITIES_H
#define OOP5_TESTUTILITIES_H

#include "../../MatrixOperations.h"

template<typename, typename>
struct AssertInt;

template<int N1, int N2>
struct AssertInt<Int<N1>, Int<N2>> {
    static_assert(N1 == N2, "Int does not match.");
};

template<typename, typename>
struct AssertList;

template<typename T, typename ...TT, typename T2, typename...TT2>
struct AssertList<List<T, TT...>, List<T2, TT2...>> {
private:
    static_assert(List<T, TT...>::size == List<T2, TT2...>::size,
                  "List size does not match.");
    static_assert(T::value == T2::value, "List value does not match.");
    AssertList<List<TT...>, List<TT2...>> assert;
};

template<>
struct AssertList<List<>, List<>> {
};

template<typename, typename>
struct AssertMatrix;

template<typename T, typename ...TT, typename T2, typename...TT2>
struct AssertMatrix<List<T, TT...>, List<T2, TT2...>> {
private:
    static_assert(List<T, TT...>::size == List<T2, TT2...>::size,
                  "Matrix size does not match.");
    AssertList<T, T2> assertRow;
    AssertMatrix<List<TT...>, List<TT2...>> assertRest;
};

template<>
struct AssertMatrix<List<>, List<>> {
};

#endif //OOP5_TESTUTILITIES_H
