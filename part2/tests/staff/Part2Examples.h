#ifndef OOP5_PART2EXAMPLES_H
#define OOP5_PART2EXAMPLES_H

#include <sstream>
#include "../../Subject.h"
#include "../my/TestUtils.h"

static int nextId;
static std::stringstream stringstream;

class Part2Examples {
private:

    class TemperatureSensor : public Subject<int> {
    };

    class AirConditioner : public Observer<int> {

        int id;

        void onTemperatureChange(int temperature) {
            stringstream << "Air Conditioner #" << id
                         << " got a report from TemperatureSensor, reading "
                         << temperature
                         << std::endl;
        }

    public:
        AirConditioner() : Observer() {
            id = (++nextId);
        }

        void handleEvent(const int &param) override {
            onTemperatureChange(param);
        }
    };

public:
    void mainPart2Examples() {
        nextId = 0;

        TemperatureSensor s;
        AirConditioner a, b, c;

        (((s += a) += b) += c);

        s(42);
        // Air Conditioner #1 got a report from TemperatureSensor, reading 42
        // Air Conditioner #2 got a report from TemperatureSensor, reading 42
        // Air Conditioner #3 got a report from TemperatureSensor, reading 42

        std::string expected = "Air Conditioner #1 got a report from TemperatureSensor, reading 42\n"
                               "Air Conditioner #2 got a report from TemperatureSensor, reading 42\n"
                               "Air Conditioner #3 got a report from TemperatureSensor, reading 42\n";
        std::string actual = stringstream.str();
        assertTest(expected == actual);

        assertException((s += a), ObserverAlreadyKnownToSubject);
        AirConditioner d;
        assertException(s -= d, ObserverUnknownToSubject);
    }
};

#endif //OOP5_PART2EXAMPLES_H
