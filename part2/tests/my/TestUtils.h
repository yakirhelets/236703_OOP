#ifndef TESTUTILS_H
#define TESTUTILS_H

#include <cstdio>

#define assertTest(expression)\
    do {\
        if (!(expression)) {\
            printf("Assertion failed at %s:%d: "\
                   "in function \"%s\" "\
                   "with assertion \"%s\".\n",\
                   __FILE__, __LINE__, __func__, (#expression));\
        }\
    } while (0)

#define assertException(command, exception)\
    do {\
        try {\
            (command);\
            assertTest(false);\
        } catch (const exception &) {\
        } catch (...) {\
            assertTest(false);\
        }\
    } while (0)

#endif //TESTUTILS_H
