#ifndef SUBJECT_H
#define SUBJECT_H

#include <vector>
#include <algorithm>
#include "Observer.h"
#include "OOP5EventException.h"

template<typename T>
class Subject{
    std::vector<Observer<T>*> obs;
public:
	Subject(){};
    void notify(const T& msg){
        for(auto ob : obs){
            ob->handleEvent(msg);
        }
    }
    void addObserver(Observer<T>& ob){
        if ( std::find(obs.begin(), obs.end(), &ob) == obs.end() ){
            obs.push_back(&ob);
        }else{
            throw ObserverAlreadyKnownToSubject();
        }
    };
    void removeObserver(Observer<T>& ob){
        if ( std::find(obs.begin(), obs.end(), &ob) == obs.end() ){
            throw ObserverUnknownToSubject();
        }else{
            obs.erase(std::remove(obs.begin(), obs.end(), &ob), obs.end());
        }
    };
    Subject<T>& operator+=(Observer<T>& ob){
        this->addObserver(ob);
        return *this;
    };
    Subject<T>& operator-=(Observer<T>& ob){
        this->removeObserver(ob);
        return *this;
    };
    Subject<T>& operator()(const T& t){
        this->notify(t);
        return *this;
    };
};

#endif