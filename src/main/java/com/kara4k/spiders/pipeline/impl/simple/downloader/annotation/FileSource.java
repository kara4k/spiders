package com.kara4k.spiders.pipeline.impl.simple.downloader.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSource {
}
