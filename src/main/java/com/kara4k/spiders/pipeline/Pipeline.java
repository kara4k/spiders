package com.kara4k.spiders.pipeline;

import com.kara4k.spiders.lifecycle.Lifecycle;

public interface Pipeline<T> {

    void onPrepare();

    void processData(final Iterable<T> iterable);

    void onFinish();

    void setLifecycle(Lifecycle lifecycle);

}
