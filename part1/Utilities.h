//
// Created by yakir on 10/01/2019.
//

#ifndef INC_236703_HW5_UTILITIES_H
#define INC_236703_HW5_UTILITIES_H

template<typename T, typename... TT>
struct List {
    T head;
    List<TT...> next;
    int size = sizeof... (TT);
};

template <typename T>
class PrependList {
    T type;
    List<T> list;
};

template <typename T>
class ListGet {
    int index;
    List<T> list;
};

template <typename T>
class ListSet {
    int index;
    List<T> list;
};

template <typename N>
struct Int {
    int value = N;
};

#endif //INC_236703_HW5_UTILITIES_H
