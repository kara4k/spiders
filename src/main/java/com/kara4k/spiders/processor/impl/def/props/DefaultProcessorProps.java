package com.kara4k.spiders.processor.impl.def.props;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Component
public class DefaultProcessorProps {

    @Value("${auth.url:}")
    private String authUrl;

    @Value("#{'${urls}'.split(';')}")
    private Set<String> urls;

    @Value("${urls.delay:0}")
    private long urlsDelay;

}
