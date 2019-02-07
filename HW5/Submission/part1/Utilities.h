//
// Created by yakir on 10/01/2019.
//

#ifndef INC_236703_HW5_UTILITIES_H
#define INC_236703_HW5_UTILITIES_H

//--------------------------List------------------------------------

template<typename...>
struct List;

template<typename T,typename... TT>
struct List<T,TT...>{
    typedef T head;
    typedef List<TT...> next;
    constexpr static int size = 1 + sizeof...(TT);
};

//specialization for empty list
template<>
struct List<>{
    constexpr static int size = 0;
};

//--------------------------PrependList------------------------------------

template<typename,typename...>
struct PrependList;

template<typename T1,typename T2,typename... TT>
struct PrependList<T1,List<T2,TT...>>{
    typedef List<T1,T2,TT...> list;
};

//specialization for empty list
template<typename T>
struct PrependList<T,List<>>{
    typedef List<T> list;
};

//--------------------------ListGet------------------------------------

template<int,typename>
struct ListGet;

template<int N,typename T,typename... TT>
struct ListGet<N,List<T,TT...>>{
    typedef typename ListGet<N-1, List<TT...>>::value value;
};

//specialization to get the requested index
template<typename T,typename... TT>
struct ListGet<0,List<T,TT...>>{
    typedef T value;
};
//we dont need specialization for an empty list -> index out of bound wont be checked!

//--------------------------ListSet------------------------------------

template<int,typename,typename>
struct ListSet;

template<int N,typename S,typename T,typename... TT>
struct ListSet<N,S,List<T,TT...>>{
    typedef typename PrependList<T,typename ListSet<N-1,S,List<TT...>>::list>::list list;
};

template<typename S,typename T,typename... TT>
struct ListSet<0,S,List<T,TT...>>{
    typedef typename PrependList<S,List<TT...>>::list list;
};

//specialization for an empty list
template<typename S>
struct ListSet<0,S,List<>>{
    typedef typename PrependList<S,List<>>::list list;
};

//--------------------------Int<N>------------------------------------

template<int>
struct Int;

template<int N>
struct Int{
    constexpr static int value = N;
};

#endif //INC_236703_HW5_UTILITIES_H
