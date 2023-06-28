package com.familidata.sbnwas_cma.rx

import io.reactivex.disposables.Disposable

//import rx.Subscription

class DisposeBag {
    private var subscriptions: ArrayList<Disposable> = ArrayList()
    private var disposeBags: ArrayList<DisposeBag> = ArrayList()

    fun dispose() {
        subscriptions.forEach { s -> s.dispose() }
        subscriptions.clear()

        disposeBags.forEach(DisposeBag::dispose)
        disposeBags.clear()

    }

    fun add(bag: DisposeBag) {
        disposeBags.add(bag)
    }

    fun add(sub: Disposable) {
        subscriptions.add(sub)
    }

}

fun Disposable.addTo(disposeBag: DisposeBag) {
    disposeBag.add(this)
}

