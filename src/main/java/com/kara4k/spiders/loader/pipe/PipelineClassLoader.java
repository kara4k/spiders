package com.kara4k.spiders.loader.pipe;

import com.kara4k.spiders.annotation.Pipe;
import com.kara4k.spiders.lifecycle.Lifecycle;
import com.kara4k.spiders.loader.AbstractGroovyClassLoader;
import com.kara4k.spiders.pipeline.Pipeline;
import com.kara4k.spiders.pipeline.included.IncludedPipelines;
import com.kara4k.spiders.pipeline.proxy.DefaultPipelineProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Aliaksei Voitsel
 */
@Component
public class PipelineClassLoader extends AbstractGroovyClassLoader<Pipeline> {

    @Value("#{'${pipelines}'.split(';')}")
    private Set<String> pipelines;
    @Value("${path.pipes:pipes}")
    private String pipesFolder;

    private Lifecycle lifecycle;
    private final IncludedPipelines includedPipelines;
    private String pipeName;

    @Autowired
    public PipelineClassLoader(final IncludedPipelines includedPipelines, final Lifecycle lifecycle) {
        this.includedPipelines = includedPipelines;
        this.lifecycle = lifecycle;
    }

    @Bean
    public Set<Pipeline> providePipelines() {
        final Set<Pipeline> pipelineList = new HashSet<>();
        pipelines.forEach(name -> {
            final Pipeline pipeline = Optional.ofNullable(includedPipelines.getPipeline(name.trim()))
                .orElseGet(groovyPipesSupplier(name.trim()));
            pipelineList.add(new DefaultPipelineProxy(pipeline));
        });

        return pipelineList;
    }

    private Supplier<Pipeline> groovyPipesSupplier(final String name) {
        return () -> {
            this.pipeName = name;
            final Pipeline instance = createInstance(pipesFolder);
            instance.setLifecycle(lifecycle);
            return instance;
        };
    }

    @Override
    protected boolean isSpecified(final Class aClass) {
        final Pipe annotation = (Pipe) aClass.getAnnotation(Pipe.class);
        return pipeName.equals(annotation.name());
    }

    @Override
    protected boolean isAssignable(final Class aClass) {
        return aClass.isAnnotationPresent(Pipe.class) && Pipeline.class.isAssignableFrom(aClass);
    }
}
