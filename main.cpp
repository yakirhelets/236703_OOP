#include <iostream>

#include "part2/tests/Part2Main.h"
#include "part1/tests/Part1Main.h"

#include "Tests/PartOneTests/partOneMatrixSetGetTests.h"
#include "Tests/PartOneTests/partOneMatrixTest.h"
#include "Tests/PartOneTests/partOneUtilityTests.h"

#include "Tests/PartTwoTests/partTwoObserverTests.h"
#include "Tests/PartTwoTests/partTwoSubjectTests.h"

int main() {
    part1Main();
    part2Main();
    
    
    //SERG Tests
    utility_test();
    get_set_matrix_test();
    matrix_test();
    
    //Part Two
    observer_test();
    subject_test();
    
    
    return 0;
}