//
// Created by yakir on 10/01/2019.
//

#ifndef INC_236703_HW5_UTILITIES_H
#define INC_236703_HW5_UTILITIES_H

template <typename T>
class List {
    T head;
    T next;
    int size;
public:
    List(T head, T next, int size) : head(head), next(next), size(size) {}
};

template <typename T>
class PrepentList {
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

template <typename T>
class Int {
    int value;
public:
    Int(int value) : value(value) {}
};

#endif //INC_236703_HW5_UTILITIES_H
