#ifndef OBSERVER_H
#define OBSERVER_H

template<typename T>
class Observer{
public:
	Observer(){};
    virtual void handleEvent(const T&) = 0;
};

#endif