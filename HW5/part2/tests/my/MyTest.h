#ifndef OOP5_MYTEST_H
#define OOP5_MYTEST_H

#include "TestUtils.h"
#include "../../Subject.h"
#include "../../Observer.h"
#include "../../OOP5EventException.h"

static int count;

class MyTest {
public:
    MyTest() : subject(nullptr), observer1(nullptr), observer2(nullptr), observer3(nullptr) {
    }

    ~MyTest() {
        deleteAll();
    }

    void test() {
        reset();
        notify_success();
        reset();
        notify_order();
        reset();
        addObserver_success();
        reset();
        addObserver_ObserverAlreadyKnownToSubject();
        reset();
        removeObserver_success();
        reset();
        removeObserver_ObserverUnknownToSubject();
        reset();
        opPlusEquals_success();
        reset();
        opPlusEquals_ObserverAlreadyKnownToSubject();
        reset();
        opPlusEquals_returnSubject();
        reset();
        opPlusEquals_returnSubjectAfterAdding();
        reset();
        opMinusEquals_success();
        reset();
        opMinusEquals_ObserverUnknownToSubject();
        reset();
        opMinusEquals_returnSubject();
        reset();
        opMinusEquals_returnSubjectAfterRemoving();
        reset();
        opParentheses_success();
        reset();
        opParentheses_order();
        reset();
        opParentheses_returnSubject();
        reset();
    }

private:

    class Object {
    };

    class ObserverTest : public Observer<Object> {
    private:
        int value;

    public:
        explicit ObserverTest() : value(0) {
        }

        int getCount() {
            return value;
        }

        void handleEvent(const Object &object) override {
            this->value = ++count;
        }
    };

    Subject<Object> *subject;

    ObserverTest *observer1;
    ObserverTest *observer2;
    ObserverTest *observer3;

    void reset() {
        count = 0;

        if (subject != nullptr) {
            delete (subject);
        }
        subject = new Subject<Object>;

        if (observer1 != nullptr) {
            delete (observer1);
        }
        observer1 = new ObserverTest;

        if (observer2 != nullptr) {
            delete (observer2);
        }
        observer2 = new ObserverTest;

        if (observer3 != nullptr) {
            delete (observer3);
        }
        observer3 = new ObserverTest;
    }

    void deleteAll() {
        if (subject != nullptr) {
            delete (subject);
        }
        subject = nullptr;

        if (observer1 != nullptr) {
            delete (observer1);
        }
        observer1 = nullptr;

        if (observer2 != nullptr) {
            delete (observer2);
        }
        observer2 = nullptr;

        if (observer3 != nullptr) {
            delete (observer3);
        }
        observer3 = nullptr;
    }

    void notify_success() {
        *subject += (*observer1);
        subject->notify(Object());
        assertTest(observer1->getCount() == 1);
    }

    void notify_order() {
        *subject += (*observer1);
        *subject += (*observer2);
        *subject += (*observer3);
        subject->notify(Object());
        assertTest(observer1->getCount() == 1);
        assertTest(observer2->getCount() == 2);
        assertTest(observer3->getCount() == 3);
    }

    void addObserver_success() {
        subject->addObserver(*observer1);
    }

    void addObserver_ObserverAlreadyKnownToSubject() {
        subject->addObserver(*observer1);
        assertException(subject->addObserver(*observer1), ObserverAlreadyKnownToSubject);
    }

    void removeObserver_success() {
        subject->addObserver(*observer1);
        subject->removeObserver(*observer1);
    }

    void removeObserver_ObserverUnknownToSubject() {
        assertException(subject->removeObserver(*observer1), ObserverUnknownToSubject);
    }

    void opPlusEquals_success() {
        *subject += *observer1;
    }

    void opPlusEquals_ObserverAlreadyKnownToSubject() {
        *subject += *observer1;
        assertException(*subject += *observer1, ObserverAlreadyKnownToSubject);
    }

    void opPlusEquals_returnSubject() {
        (*subject += *observer1) += *observer2;
    }

    void opPlusEquals_returnSubjectAfterAdding() {
        assertException((*subject += *observer1) += *observer1, ObserverAlreadyKnownToSubject);
    }

    void opMinusEquals_success() {
        *subject += *observer1;
        *subject -= *observer1;
    }

    void opMinusEquals_ObserverUnknownToSubject() {
        assertException(*subject -= *observer1, ObserverUnknownToSubject);
    }

    void opMinusEquals_returnSubject() {
        *subject += *observer1;
        *subject += *observer2;
        (*subject -= *observer1) -= *observer2;
    }

    void opMinusEquals_returnSubjectAfterRemoving() {
        *subject += *observer1;
        assertException((*subject -= *observer1) -= *observer1, ObserverUnknownToSubject);
    }

    void opParentheses_success() {
        *subject += (*observer1);
        subject->operator()(Object());
        assertTest(observer1->getCount() == 1);
    }

    void opParentheses_order() {
        *subject += (*observer1);
        *subject += (*observer2);
        *subject += (*observer3);
        subject->operator()(Object());
        assertTest(observer1->getCount() == 1);
        assertTest(observer2->getCount() == 2);
        assertTest(observer3->getCount() == 3);
    }

    void opParentheses_returnSubject() {
        *subject += (*observer1);
        *subject += (*observer2);
        (subject->operator()(Object())).operator()(Object());
        assertTest(observer1->getCount() == 3);
        assertTest(observer2->getCount() == 4);
    }
};

#endif //OOP5_MYTEST_H
