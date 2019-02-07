#ifndef OOP5_MATRIXOPERATIONSTEST_H
#define OOP5_MATRIXOPERATIONSTEST_H

#include "TestUtilities.h"

class MatrixOperationsTest {
private:
    void add() {
        typedef List<
                List<Int<1>, Int<2>, Int<3>>,
                List<Int<4>, Int<5>, Int<6>>,
                List<Int<7>, Int<8>, Int<9>>
        > matrix1;

        typedef List<
                List<Int<10>, Int<11>, Int<12>>,
                List<Int<13>, Int<14>, Int<15>>,
                List<Int<16>, Int<17>, Int<18>>
        > matrix2;

        typedef List<
                List<Int<11>, Int<13>, Int<15>>,
                List<Int<17>, Int<19>, Int<21>>,
                List<Int<23>, Int<25>, Int<27>>
        > expected;

        typedef Add<matrix1, matrix2>::result actual;

        AssertMatrix<expected, actual> assert;
    }

    void add_staff() {
        typedef List<
                List<Int<1>, Int<2>, Int<0> >,
                List<Int<0>, Int<1>, Int<0> >,
                List<Int<0>, Int<0>, Int<5> >
        > matrix1;

        typedef List<
                List<Int<7>, Int<6>, Int<0> >,
                List<Int<0>, Int<7>, Int<0> >,
                List<Int<8>, Int<0>, Int<3> >
        > matrix2;

        typedef typename Add<matrix1, matrix2>::result matrix3;

        typedef List<
                List<Int<8>, Int<8>, Int<0> >,
                List<Int<0>, Int<8>, Int<0> >,
                List<Int<8>, Int<0>, Int<8> >
        > expected;

        AssertMatrix<expected, matrix3> assertMatrix;
    }

    void transpose() {
        typedef List<
                List<Int<1>, Int<2>, Int<3>>,
                List<Int<4>, Int<5>, Int<6>>
        > matrix;

        typedef List<
                List<Int<1>, Int<4>>,
                List<Int<2>, Int<5>>,
                List<Int<3>, Int<6>>
        > expected;

        typedef Transpose<matrix>::matrix actual;

        AssertMatrix<expected, actual> assertTranspose;
    }

    void multiply() {
        typedef List<
                List<Int<2>, Int<3>, Int<4>>,
                List<Int<4>, Int<5>, Int<6>>
        > matrix1;

        typedef List<
                List<Int<7>, Int<10>, Int<13>>,
                List<Int<8>, Int<11>, Int<14>>,
                List<Int<9>, Int<12>, Int<15>>
        > matrix2;

        typedef List<
                List<Int<74>, Int<101>, Int<128>>,
                List<Int<122>, Int<167>, Int<212>>
        > excepted;

        typedef Multiply<matrix1, matrix2>::result actual;

        AssertMatrix<excepted, actual> assertMatrix;
    }

    void multiply_staff() {
        typedef List<
                List<Int<1>, Int<2> >,
                List<Int<0>, Int<1> >
        > matrix1;

        typedef List<
                List<Int<0>, Int<7> >,
                List<Int<8>, Int<0> >
        > matrix2;

        typedef typename Multiply<matrix1, matrix2>::result matrix3;

        typedef List<
                List<Int<16>, Int<7> >,
                List<Int<8>, Int<0> >
        > expected;

        AssertMatrix<expected, matrix3> assertMatrix;
    }
};

#endif //OOP5_MATRIXOPERATIONSTEST_H
