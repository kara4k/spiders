package com.kara4k.spiders.spider;

import com.kara4k.spiders.processor.Processor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Log
public abstract class AbstractSpider<T extends Iterable> implements Spider<T> {

    @Getter
    private String url;
    @Setter
    private Processor processor;


    @Override
    public void onPrepare() {
    }

    @Override
    public void onAuth(final String url) {

    }

    public void onInitUrl(final String url) {
        log.info(url);
        this.url = url;
    }

    @Override
    public T collectData(final String url) {
        onInitUrl(url);
        return crawl(url);
    }

    @Override
    public void flush(final T t) {
        processor.onFlush(t);
    }

    @Override
    public Set<String> follow() {
        return null;
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onExit() {

    }


}
