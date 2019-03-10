package com.kara4k.spiders.spider;

import com.kara4k.spiders.processor.Processor;
import io.reactivex.Scheduler;

import java.util.Set;

public interface Spider<T extends Iterable> {

    void onPrepare();

    void onAuth(String url);

    void onInitUrl(final String url);

    T collectData(String url);

    T crawl(String url);

    void flush(T t);

    Set<String> follow();

    void onComplete();

    void onExit();

    Scheduler getScheduler();

    void setProcessor(Processor processor);
}
