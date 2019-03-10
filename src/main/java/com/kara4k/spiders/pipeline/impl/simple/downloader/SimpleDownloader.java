package com.kara4k.spiders.pipeline.impl.simple.downloader;

import com.kara4k.spiders.annotation.Pipe;
import com.kara4k.spiders.pipeline.AbstractPipeline;
import com.kara4k.spiders.pipeline.impl.simple.downloader.annotation.FileName;
import com.kara4k.spiders.pipeline.impl.simple.downloader.annotation.FileSource;
import com.kara4k.spiders.pipeline.impl.simple.downloader.annotation.SaveFolder;
import com.kara4k.spiders.utils.FileAnnotationUtils;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Log
@Service(SimpleDownloader.NAME)
@Pipe(name = SimpleDownloader.NAME)
public class SimpleDownloader extends AbstractPipeline<Object> {

    public static final String NAME = "download";

    @Value("${baseFolder}")
    private String baseSaveFolder;

    @Value("${spider}")
    private String serviceName;

    private int count = 0;

    @Override
    public void onPrepare() {

    }

    @Override
    protected Scheduler getScheduler() {
        return Schedulers.io();
    }

    @Override
    public void onNext(final Object o) {
        handleItem(o);
        count++;
    }

    private void handleItem(final Object o) {
        final String saveFolder = FileAnnotationUtils.getFieldStringValue(o, SaveFolder.class, serviceName);
        final String sourceUrl = FileAnnotationUtils.getFieldStringValue(o, FileSource.class);
        final String fileName = FileAnnotationUtils.getFieldStringValue(o, FileName.class, String.valueOf(o.hashCode()));

        final Path fullPath = getFullSavePath(saveFolder, fileName);
        createFolder(fullPath);
        download(fullPath, sourceUrl);
    }

    private void download(final Path savePath, final String src) {
        InputStream in = null;

        try {
            in = new URL(src).openStream();
            Files.copy(in, savePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Downloaded: " + savePath.toString());
        } catch (final IOException e) {
            log.severe(e.getMessage());
        }
    }

    private Path getFullSavePath(final String saveFolder, final String fileName) {
        return Paths.get(
            String.format("%s/%s/%s/%s/", baseSaveFolder, serviceName, saveFolder, fileName));
    }

    private void createFolder(final Path path) {
        try {
            Files.createDirectories(path.getParent());
        } catch (final IOException e) {
            log.severe(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void onFinish() {
        log.info(String.format("Downloaded: %d files", count));
    }

}
