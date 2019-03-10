package com.kara4k.spiders.pipeline.impl.simple.database;

import com.kara4k.spiders.annotation.Pipe;
import com.kara4k.spiders.pipeline.AbstractPipeline;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * @author Aliaksei Voitsel
 */

@Pipe(name = SimpleDBSaver.NAME)
@Service(SimpleDBSaver.NAME)
public class SimpleDBSaver<T> extends AbstractPipeline<T> {

    public static final String NAME = "sql";

    @Autowired
    SQLightService<T> sqLightService;
    @Value("${db.url}")
    private String dbUrl;

    @Override
    public void onPrepare() {
        try {
            sqLightService.init(dbUrl);
        } catch (final SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void onNext(final T t) {
        try {
            sqLightService.add(t);
        } catch (final SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void onFinish() {
        try {
            sqLightService.close();
        } catch (final SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected Scheduler getScheduler() {
        return Schedulers.trampoline();
    }
}

