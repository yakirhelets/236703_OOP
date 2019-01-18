//
// Created by yakir on 10/01/2019.
//

#ifndef INC_236703_HW5_MATRIXOPERATIONS_H
#define INC_236703_HW5_MATRIXOPERATIONS_H

#include "Utilities.h"

//--------------------------MatrixGet------------------------------------

template<typename,typename,typename>
struct MatrixGet;

template<typename I,typename J,typename M>
struct MatrixGet{
    typedef typename ListGet<J, typename ListGet<I,M>::value>::value value;
};

//--------------------------MatrixSet------------------------------------

template<typename,typename,typename,typename>
struct MatrixSet;

template<typename I,typename J,typename S,typename M>
struct MatrixSet{
    typedef typename ListSet<I,ListSet<J,S,ListGet<I,M>::list>::list,M>::list list;
};


#endif //INC_236703_HW5_MATRIXOPERATIONS_H
