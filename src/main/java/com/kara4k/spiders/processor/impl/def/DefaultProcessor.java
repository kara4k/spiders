package com.kara4k.spiders.processor.impl.def;

import com.kara4k.spiders.lifecycle.Lifecycle;
import com.kara4k.spiders.loader.pipe.PipelineClassLoader;
import com.kara4k.spiders.pipeline.Pipeline;
import com.kara4k.spiders.processor.Processor;
import com.kara4k.spiders.processor.impl.def.filter.FollowFilter;
import com.kara4k.spiders.processor.impl.def.props.DefaultProcessorProps;
import com.kara4k.spiders.selenide.SelenideConfig;
import com.kara4k.spiders.spider.Spider;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

import static com.kara4k.spiders.lifecycle.Lifecycle.Service.SPIDER;

@Log
@Component
public class DefaultProcessor implements Processor, Observer<String>, Lifecycle.Finishable {

    @Autowired
    DefaultProcessorProps props;
    @Autowired
    private Spider spider;
    @Autowired
    private PipelineClassLoader pipelineClassLoader;
    private Set<Pipeline> pipelines;
    @Autowired
    private FollowFilter followFilter;
    @Autowired
    private Lifecycle lifecycle;
    @Autowired
    private SelenideConfig selenideConfig;

    public void start() {
        init();
        spider.onAuth(props.getAuthUrl());
        collectData(props.getUrls());
        lifecycle.sleepUntilFinished();
    }

    private void init() {
        pipelines = pipelineClassLoader.providePipelines();
        selenideConfig.init();
        spider.setProcessor(this);
        spider.onPrepare();
        pipelines.forEach(Pipeline::onPrepare);
    }

    private void collectData(final Collection<String> urls) {
        Observable.fromIterable(urls)
            .subscribeOn(spider.getScheduler())
            .doAfterNext(x -> {
                while (lifecycle.isLocked()) {
                    Thread.sleep(1000);
                }
            }).doAfterNext(x -> Thread.sleep(props.getUrlsDelay()))
            .subscribe(this);
    }

    @Override
    public <T extends Iterable> void onFlush(final T t) {
        passThroughPipes(t);
    }

    @Override
    public void onSubscribe(final Disposable disposable) {
        lifecycle.started(SPIDER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onNext(final String url) {
        final Iterable iterable = spider.collectData(url.trim());
        passThroughPipes(iterable);
        followFilter.filter(spider.follow());
    }

    @Override
    public void onError(final Throwable throwable) {
        throwable.printStackTrace();
        log.severe(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        final Set<String> urlsToFollow = followFilter.getUrlsToFollow();

        if (urlsToFollow.size() != 0) {
            collectData(urlsToFollow);
        }

        lifecycle.checkForFinish(SPIDER);
    }

    private void passThroughPipes(final Iterable iterable) {
        Observable.fromIterable(pipelines)
            .subscribeOn(Schedulers.io())
            .subscribe(pipeline -> pipeline.processData(iterable), this::onError);
    }

    @Override
    public void onFinish() {
        spider.onComplete(); // for global Task in future
        spider.onExit();
        pipelines.forEach(Pipeline::onFinish);

        System.exit(0);
    }
}
