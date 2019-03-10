package com.kara4k.spiders.spider;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Log
public abstract class JsoupSpider<T extends Iterable> extends AbstractSpider<T> { // TODO: 10.03.2019

    @Getter
    private Document document;

    @Override
    public void onInitUrl(final String url) {
        super.onInitUrl(url);
        try {
            document = Jsoup.connect(url).get();
        } catch (final IOException e) {
            log.severe(e.getMessage());
            document = null;
        }
    }

    @Override
    public Scheduler getScheduler() {
        return Schedulers.io();
    }
}
