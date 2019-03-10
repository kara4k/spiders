package com.kara4k.spiders.lifecycle;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log
@Component
public class Lifecycle {

    @Autowired
    private Props props;

    private int spidersCount;
    private int pipesCount;

    private Finishable finishable;

    @Autowired
    public Lifecycle(final Finishable finishable) {
        this.finishable = finishable;
    }

    /**
     * Temporarily solution.
     * <p>
     * Right now, i have no idea how to keep main thread alive, and wait for another threads finish it's work,
     * in another true way.
     */
    public void sleepUntilFinished() {
        try {
            System.in.read();
        } catch (final IOException e) {
            log.severe(e.getMessage());
        }
    }

    public void started(final Service service) {
        apply(service, true);
    }

    public void checkForFinish(final Service service) {
        apply(service, false);
        log.info(String.format("Finish check. Spiders: %d, Pipes: %d", spidersCount, pipesCount));

        if (spidersCount == 0 && pipesCount == 0) {
            log.info("Done");

            if (finishable != null) {
                finishable.onFinish();
            }
        }
    }

    public boolean isLocked() { // TODO: 11/15/18 think of + schedulers extract trampoline to keep order or threading
        return spidersCount > props.getMaxSpiders() || pipesCount > props.getMaxPipes();
    }

    private void apply(final Service service, final boolean isAdd) {
        switch (service) {
            case SPIDER:
                spidersCount = isAdd ? ++spidersCount : --spidersCount;
                break;
            case PIPELINE:
                pipesCount = isAdd ? ++pipesCount : --pipesCount;
        }
    }

    public enum Service {
        SPIDER, PIPELINE
    }

    public interface Finishable {
        void onFinish();
    }

}
