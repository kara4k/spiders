package com.kara4k.spiders.spider.proxy;

import com.kara4k.spiders.processor.Processor;
import com.kara4k.spiders.spider.Spider;
import io.reactivex.Scheduler;

import java.util.Set;

public class DefaultSpiderProxy implements Spider {

    private final Spider spider;

    public DefaultSpiderProxy(final Spider spider) {
        this.spider = spider;
    }

    @Override
    public void onPrepare() {
        spider.onPrepare();
    }

    @Override
    public void onAuth(final String url) {
        spider.onAuth(url);
    }

    @Override
    public void onInitUrl(final String url) {
        spider.onInitUrl(url);
    }

    @Override
    public Iterable collectData(final String url) {
        return spider.collectData(url);
    }

    @Override
    public Iterable crawl(final String url) {
        return spider.crawl(url);
    }

    @Override
    public void flush(final Iterable iterable) {
        spider.flush(iterable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> follow() {
        return spider.follow();
    }

    @Override
    public void onComplete() {
        spider.onComplete();
    }

    @Override
    public void onExit() {
        spider.onExit();
    }

    @Override
    public Scheduler getScheduler() {
        return spider.getScheduler();
    }

    @Override
    public void setProcessor(final Processor processor) {
        spider.setProcessor(processor);
    }
}
