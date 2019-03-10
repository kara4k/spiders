package com.kara4k.spiders;

import com.kara4k.spiders.processor.Processor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log
@SpringBootApplication(scanBasePackages = "com.kara4k.spiders")
public class Application implements CommandLineRunner {

    private final Processor processor;

    @Autowired
    public Application(final Processor processor) {
        this.processor = processor;
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(final String... args) {
        processor.start();
    }

}
