package com.arch.domain

import com.arch.portdomain.model.EnumStateLayer
import com.arch.portdomain.model.StateLayer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject


abstract class BaseInteractor  {
    private val publish : PublishSubject<StateLayer> = PublishSubject.create()
    private var jobThread: Scheduler = Schedulers.io()
    protected open fun provideSchedulersIO() : Scheduler = jobThread
    fun onSuccess(state : StateLayer) = publish.onNext(state)
    fun onError(state : StateLayer) = state.message.let {publish.onError(Throwable(it))}
    abstract fun byDomain() : Observable<StateLayer>
    fun observationState(): Observable<StateLayer> =
        Observable.defer{publish}
            .subscribeOn(provideSchedulersIO())
            .filter{it.status != 0}
    fun reset() = publish.onNext(StateLayer(EnumStateLayer.RESET.const))


}