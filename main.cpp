#include <iostream>
#include <assert.h>

// #include "part2/tests/Part2Main.h"
#include "part1/tests/Part1Main.h"

#include "Tests/PartOneTests/partOneMatrixSetGetTests.h"
#include "Tests/PartOneTests/partOneMatrixTest.h"
#include "Tests/PartOneTests/partOneUtilityTests.h"

// #include "Tests/PartTwoTests/partTwoObserverTests.h"
// #include "Tests/PartTwoTests/partTwoSubjectTests.h"

#include "part1/Utilities.h"

int main() {
    // typedef List<Int<1>,Int<2>,Int<3>,Int<4>> types;
    // typedef typename ListSet<2, Int<7>, types>::list listA;
    // static_assert(types::next::next::head::value ==  3, "Not the answer!");
    // static_assert(listA::next::next::head::value ==  7, "Not the answer!");
    // //static_assert( typeid(types::head) == typeid(Int<1>));

    part1Main();
    // part2Main();
    
    
    //SERG Tests
    utility_test();
    get_set_matrix_test();
    matrix_test();
    
    // //Part Two
    // observer_test();
    // subject_test();
    
    
    return 0;
}