//
// Created by yakir on 10/01/2019.
//

#ifndef INC_236703_HW5_UTILITIES_H
#define INC_236703_HW5_UTILITIES_H


template<typename T, typename... TT>
struct List {
    typedef T head;
    typedef List<TT...> next;
    //int size = sizeof... (TT);
};

// template <typename T>
// class PrependList {
//     T type;
//     List<T> list;
// };

// template <typename T>
// class ListGet {
//     int index;
//     List<T> list;
// };

// template <typename T>
// class ListSet {
//     int index;
//     List<T> list;
// };

template <int>
struct Int;

template <int N>
struct Int {
    constexpr static int val = N;
};

#endif //INC_236703_HW5_UTILITIES_H
