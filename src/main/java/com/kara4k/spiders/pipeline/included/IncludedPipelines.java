package com.kara4k.spiders.pipeline.included;

import com.kara4k.spiders.pipeline.Pipeline;
import com.kara4k.spiders.pipeline.impl.simple.database.SimpleDBSaver;
import com.kara4k.spiders.pipeline.impl.simple.downloader.SimpleDownloader;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Aliaksei Voitsel
 */
@Getter
@Component
public class IncludedPipelines {

    @Autowired
    @Qualifier(value = SimpleDownloader.NAME)
    Pipeline simpleDownloader;
    @Autowired
    @Qualifier(SimpleDBSaver.NAME)
    Pipeline simpleDbSaver;

    public Pipeline getPipeline(final String name){
        if (name.equals(SimpleDownloader.NAME)) return simpleDownloader;
        else if (name.equals(SimpleDBSaver.NAME)) return simpleDbSaver;

        return null;
    }
}
