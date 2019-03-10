package com.kara4k.spiders.processor.impl.def.filter;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Log
@Component
public class FollowFilter {

    private final Set<String> allUrls;
    private final Set<String> followTo;

    public FollowFilter() {
        allUrls = new HashSet<>();
        followTo = new HashSet<>();
    }

    public synchronized void filter(final Set<String> links) {
        if (links == null) return;

        log.info("Total urls handled: " + allUrls.size());
        log.info("Filtering urls to follow: " + links.size());

        links.forEach(link -> {
            if (!allUrls.contains(link)) {
                followTo.add(link);
            }
        });

        allUrls.addAll(followTo);

        log.info("Filtered urls: " + followTo.size());
    }


    public Set<String> getUrlsToFollow() {
        final HashSet<String> urls = new HashSet<>(followTo);
        followTo.clear();
        return urls;
    }
}
