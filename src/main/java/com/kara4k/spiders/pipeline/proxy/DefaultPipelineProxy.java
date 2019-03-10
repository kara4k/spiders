package com.kara4k.spiders.pipeline.proxy;

import com.kara4k.spiders.lifecycle.Lifecycle;
import com.kara4k.spiders.pipeline.Pipeline;

/**
 * @author Aliaksei Voitsel
 */
public class DefaultPipelineProxy<T> implements Pipeline<T> {

    private final Pipeline<T> pipeline;

    public DefaultPipelineProxy(final Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void onPrepare() {
        pipeline.onPrepare();
    }

    @Override
    public void processData(final Iterable<T> iterable) {
        pipeline.processData(iterable);
    }

    @Override
    public void onFinish() {
        pipeline.onFinish();
    }

    @Override
    public void setLifecycle(final Lifecycle lifecycle) {
        pipeline.setLifecycle(lifecycle);
    }
}
