//
// Created by yakir on 10/01/2019.
//

#ifndef INC_236703_HW5_MATRIXOPERATIONS_H
#define INC_236703_HW5_MATRIXOPERATIONS_H

#include "Transpose.h"
#include "Utilities.h"

//--------------------------MatrixGet------------------------------------

template<int,int,typename>
struct MatrixGet;

template<int I,int J,typename M>
struct MatrixGet{
    typedef typename ListGet<J, typename ListGet<I,M>::value>::value value;
};

//--------------------------MatrixSet------------------------------------

template<int,int,typename,typename>
struct MatrixSet;

template<int I,int J,typename S,typename M>
struct MatrixSet{
    typedef typename ListSet<I,typename ListSet<J,S,typename ListGet<I,M>::value>::list,M>::list list;
};

//--------------------------MatrixAdd------------------------------------

//****Auxiliry templates for adding****
template<int,int,int,typename,typename>
struct AddAux;

template<int I,int J,int C,typename M1,typename M2>
struct AddAux{
    typedef typename MatrixGet<I,J,M1>::value val1; //getting first val
    typedef typename MatrixGet<I,J,M2>::value val2; //getting second val
    typedef typename MatrixSet<I,J,Int<val1::value+val2::value>,M1>::list tempMatrix; //getting the new matrix after setting new value
    typedef typename AddAux<I,J-1,C,tempMatrix,M2>::result result; //recursive call
};

//loop to the *next line above* to the righest val
template<int I,int C,typename M1,typename M2>
struct AddAux<I,0,C,M1,M2>{
    typedef typename MatrixGet<I,0,M1>::value val1; 
    typedef typename MatrixGet<I,0,M2>::value val2;
    typedef typename MatrixSet<I,0,Int<val1::value+val2::value>,M1>::list tempMatrix; 
    typedef typename AddAux<I-1,C,C,tempMatrix,M2>::result result; //recursive call
};

//specialization for the last value adding (0,0) - top left corner
template<int C,typename M1,typename M2>
struct AddAux<0,0,C,M1,M2>{
    typedef typename MatrixGet<0,0,M1>::value val1; 
    typedef typename MatrixGet<0,0,M2>::value val2; 
    typedef typename MatrixSet<0,0,Int<val1::value+val2::value>,M1>::list result;
};

template<typename,typename>
struct Add;

template<typename M1,typename M2>
struct Add{
    static_assert(M1::head::size == M2::head::size,"Can't add"); //check same width
    static_assert(M1::size == M2::size,"Can't add"); //check same height
    typedef typename AddAux<M1::size-1,M1::head::size-1,M1::head::size-1,M1,M2>::result result;
};



//--------------------------MatrixMultiply------------------------------------

//calculating one element in the final matrix(while M2 will be *transposed* matrix!)
template<int,int,int,typename,typename>
struct CalculateElement;

template<int I,int J,int C,typename M1,typename M2>
struct CalculateElement{
    typedef typename MatrixGet<I,C,M1>::value val1; //get val from first matrix
    typedef typename MatrixGet<J,C,M2>::value val2; //get val from second matrix
    typedef typename CalculateElement<I,J,C-1,M1,M2>::tempVal temp; //recursive call 
    typedef Int<val1::value*val2::value + temp::value> tempVal; 
};

//specialization for the final adding for the element
template<int I,int J,typename M1,typename M2>
struct CalculateElement<I,J,0,M1,M2>{
    typedef typename MatrixGet<I,0,M1>::value val1;
    typedef typename MatrixGet<J,0,M2>::value val2;
    typedef Int<val1::value*val2::value> tempVal;
};

//calculates one row in the final matrix
template<int,int,int,typename,typename,typename>
struct CalculateRow;

template<int I,int J,int C,typename M1,typename M2,typename ROW_LIST>
struct CalculateRow{
    typedef typename CalculateElement<I,J,C,M1,M2>::tempVal currentElement; //calculate the element
    typedef typename PrependList<currentElement,ROW_LIST>::list rowList; //put it in the right place in the I'th row
    typedef typename CalculateRow<I,J-1,C,M1,M2,rowList>::row row; //recursive call
};

//specialization for finishing calculating one row
template<int I,int C,typename M1,typename M2,typename ROW_LIST>
struct CalculateRow<I,0,C,M1,M2,ROW_LIST>{
    typedef typename CalculateElement<I,0,C,M1,M2>::tempVal currentElement;
    typedef typename PrependList<currentElement,ROW_LIST>::list row;
};

template<int,int,int,typename,typename,typename>
struct MultiplyAux;

template<int I,int J,int C,typename M1,typename M2,typename ROWS_LIST>
struct MultiplyAux{
    typedef typename CalculateRow<I,J,C,M1,M2,List<>>::row currentRow; //calculate one final row
    typedef typename PrependList<currentRow,ROWS_LIST>::list rowsList; //add the row to the final matrix list
    typedef typename MultiplyAux<I-1,J,C,M1,M2,rowsList>::row row; //recursive call
};

//specialization for the final row calculation
template<int J,int C,typename M1,typename M2,typename ROWS_LIST>
struct MultiplyAux<0,J,C,M1,M2,ROWS_LIST> {
    typedef typename CalculateRow<0,J,C,M1,M2,List<>>::row currentRow;
    typedef typename PrependList<currentRow,ROWS_LIST>::list row;
};

template<typename,typename>
struct Multiply;

template<typename M1,typename M2>
struct Multiply{
    static_assert(M1::head::size == M2::size, "Can't multiply"); //check if first matrix width == second matrix height
    typedef typename Transpose<M2>::matrix m2_transposed;
    typedef typename MultiplyAux<M1::size-1,m2_transposed::size-1,m2_transposed::head::size-1,M1,m2_transposed,List<>>::row result;
};


#endif //INC_236703_HW5_MATRIXOPERATIONS_H
