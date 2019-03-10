package com.kara4k.spiders.pipeline;

import com.kara4k.spiders.lifecycle.Lifecycle;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

@Log
public abstract class AbstractPipeline<T> implements Pipeline, Observer<T> {

    @Autowired
    private Lifecycle lifecycle;

    protected abstract Scheduler getScheduler();

    @Override
    @SuppressWarnings("unchecked")
    public void processData(final Iterable iterable) {
        Observable.fromIterable(iterable)
                .subscribeOn(getScheduler())
                .subscribe(this);
    }

    @Override
    public void onSubscribe(final Disposable disposable) {
        lifecycle.started(Lifecycle.Service.PIPELINE);
    }

    @Override
    public void onError(final Throwable throwable) {
        log.severe(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        lifecycle.checkForFinish(Lifecycle.Service.PIPELINE);
    }

    @Override
    public void setLifecycle(final Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }
}
