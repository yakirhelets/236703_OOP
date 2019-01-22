#ifndef SUBJECT_H
#define SUBJECT_H

#include <set>
#include "OOP5EventException.h"

using namespace std;

template<typename T>
class Subject{
    set<class Observer<T>*> obs;
public:
    Subject(); //default cons
    void notify(const T& msg){
        for (int i = 0; i < obs.size(); i++)
        obs[i]<T>->handleEvent(msg);
    }
    void addObserver(Observer<T>& ob){
        if(obs.find(ob) != set<class Observer<T>*>::end){
            obs.insert(ob);
        }else{
            throw ObserverAlreadyKnownToSubject;
        }
    };
    void removeObserver(Observer<T>& ob){
        if(obs.find(ob) == set::end){
            throw ObserverUnknownToSubject;
        }else{
            obs.erase(ob);
        }
    };
    Subject<T>& operator+=(Observer<T>& ob){
        this.addObserver(ob);
        return this;
    };
    Subject<T>& operator-=(Observer<T>& ob){
        this.removeObserver(ob);
        return this;
    };
    Subject<T>& operator()(const T& t){
        this.notify(t);
        return this;
    };
};

#endif