#ifndef SUBJECT_H
#define SUBJECT_H

#include <set>
#include "Observer.h"
#include "OOP5EventException.h"

template<typename T>
class Subject{
    std::set<Observer<T>*> obs;
public:
    void notify(const T& msg){
        for(auto ob : obs){
            ob->handleEvent(msg);
        }
    }
    void addObserver(Observer<T>& ob){
        if(obs.find(&ob) != obs.end()){
            obs.insert(&ob);
        }else{
            throw ObserverAlreadyKnownToSubject();
        }
    };
    void removeObserver(Observer<T>& ob){
        if(obs.find(&ob) == obs.end()){
            throw ObserverUnknownToSubject();
        }else{
            obs.erase(&ob);
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