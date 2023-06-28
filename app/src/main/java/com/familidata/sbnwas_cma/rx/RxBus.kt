package com.familidata.sbnwas_cma.rx

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxBus {
    private val bus = PublishSubject.create<Pair<String,String>>()
    fun send(o: Pair<String,String>) {
        bus.onNext(o)
    }

    fun toObservable(): Observable<Pair<String,String>> {
        return bus
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }
}

//data class Bus(
//    var KEY_CODE: String,
//)