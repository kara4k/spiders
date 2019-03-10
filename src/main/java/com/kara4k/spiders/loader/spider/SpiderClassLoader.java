package com.kara4k.spiders.loader.spider;

import com.kara4k.spiders.loader.AbstractGroovyClassLoader;
import com.kara4k.spiders.spider.Spider;
import com.kara4k.spiders.spider.proxy.DefaultSpiderProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author Aliaksei Voitsel
 */
@Service
public class SpiderClassLoader extends AbstractGroovyClassLoader<Spider> {

    @Value("${spider}")
    private String spiderName;

    @Bean
    public Spider provideSpider(@Value("${path.spiders:spiders}")final String spidersFolder){
        final Spider instance = createInstance(spidersFolder);
        return new DefaultSpiderProxy(instance);
    }

    @Override
    protected boolean isSpecified(final Class aClass) {
        final com.kara4k.spiders.annotation.Spider annotation
            = (com.kara4k.spiders.annotation.Spider) aClass.getAnnotation(com.kara4k.spiders.annotation.Spider.class);
        return annotation.name().equals(spiderName);
    }

    @Override
    protected boolean isAssignable(final Class aClass) {
        return aClass.isAnnotationPresent(com.kara4k.spiders.annotation.Spider.class)
               && Spider.class.isAssignableFrom(aClass);
    }
}
