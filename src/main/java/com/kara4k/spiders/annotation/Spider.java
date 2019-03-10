package com.kara4k.spiders.annotation;

import io.reactivex.Scheduler;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Spider {
    String name();
}
