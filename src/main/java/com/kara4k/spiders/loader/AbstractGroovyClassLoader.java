package com.kara4k.spiders.loader;

import groovy.lang.GroovyClassLoader;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * @author Aliaksei Voitsel
 */
@Log
public abstract class AbstractGroovyClassLoader<T> {

    protected abstract boolean isSpecified(final Class aClass);

    protected abstract boolean isAssignable(final Class aClass);


    public T createInstance(final String folder) {
        final Path path = Paths.get(new File(".").getAbsolutePath(), folder);
        final Collection<File> files = FileUtils.listFiles(new File(path.toString()),
                                                           new RegexFileFilter("^(.*)$"),
                                                           DirectoryFileFilter.DIRECTORY);
        for (final File file : files) {
            final T instance = processClass(file);
            if (instance != null) {
                return instance;
            }
        }

        throw new RuntimeException("Groovy class not found");
    }

    @SuppressWarnings("unchecked")
    private T processClass(final File file) {
        try {
            final GroovyClassLoader classLoader = new GroovyClassLoader();
            final Class aClass = classLoader.parseClass(file);
            if (isAssignable(aClass) && isSpecified(aClass)) {
                return (T) aClass.getConstructor().newInstance();
            }
        } catch (final IOException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.severe(e.getMessage());
        }
        return null;
    }

}
