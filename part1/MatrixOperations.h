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

//--------------------------MatrixAdd------------------------------------

template<typename,typename>
struct Add;

template<typename M1,typename M2>
struct Add{
    static_assert(M1::head::size == M2::head::size,"Can't add"); //check same width
    static_assert(M1::size == M2::size,"Can't add"); //check same height
    typedef typename AddAux<M1::size-1,M1::head::size-1,M1,M2,M1::head::size-1>::result result;
};

//****Auxiliry templates for adding****
template<int,int,typename,typename,int>
struct AddAux;

template<int I,int J,typename M1,typename M2,int C>
struct AddAux{
    typedef typename MatrixGet<I,J,M1>::value val1; //getting first val
    typedef typename MatrixGet<I,J,M2>::value val2; //getting second val
    typedef typename MatrixSet<I,J,Int<val1::value+val2::value>,M1>::list tempMatrix; //getting the new matrix after setting new value
    typedef typename AddAux<I,J-1,tempMatrix,M2,C>::result result; //recursive call
};

//loop to the *next line above* to the righest val
template<int I,typename M1,typename M2,int C>
struct AddAux<I,0,M1,M2,C>{
    typedef typename MatrixGet<I,0,M1>::value val1; 
    typedef typename MatrixGet<I,0,M2>::value val2;
    typedef typename MatrixSet<I,0,Int<val1::value+val2::value>,M1>::list tempMatrix; 
    typedef typename AddAux<I-1,C,tempMatrix,M2,C>::result result; //recursive call
};

//specialization for the last value adding (0,0) - top left corner
template<typename M1,typename M2,int C>
struct AddAux<0,0,M1,M2,C>{
    typedef typename MatrixGet<0,0,M1>::value val1; 
    typedef typename MatrixGet<0,0,M2>::value val2; 
    typedef typename MatrixSet<0,0,Int<val1::value+val2::value>,M1>::list result;
};

//--------------------------MatrixMultiply------------------------------------




#endif //INC_236703_HW5_MATRIXOPERATIONS_H
