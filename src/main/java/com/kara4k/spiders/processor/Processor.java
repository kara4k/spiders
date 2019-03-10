package com.kara4k.spiders.processor;

public interface Processor {

    void start();

    <T extends Iterable> void onFlush(T t);

}
