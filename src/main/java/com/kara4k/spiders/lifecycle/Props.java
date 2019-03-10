package com.kara4k.spiders.lifecycle;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Props {

    @Value("${lifecycle.maxSpiders:5}")
    private int maxPipes;

    @Value("${lifecycle.maxPipelines:5}")
    private int maxSpiders;

}
