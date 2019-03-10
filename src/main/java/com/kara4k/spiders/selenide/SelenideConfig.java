package com.kara4k.spiders.selenide;

import com.codeborne.selenide.Configuration;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Log
@Data
@Component
@PropertySource("classpath:config/selenide.properties")
public class SelenideConfig {

    @Value("${selenide.driver.path:null}")
    private String driverPath;
    @Value("${selenide.browser:chrome}")
    private String browser;
    @Value("${selenide.headless:false}")
    private boolean isHeadless;
    @Value("${selenide.timeout:60000}")
    private Long timeout;
    @Value("${selenide.maximized:true}")
    private boolean isMaximized;
    @Value("${selenide.reports.path:reports}")
    private String reportsPath;
    @Value("${selenide.screenshots:true}")
    private boolean isScreenshotsOnFail;
    @Value("${selenide.click.js:true}")
    private boolean isClickWithJS;
    @Value("${selenide.fast.set.value:true}")
    private boolean isFastSetValue;

    public void init() {
        log.info(this.toString());
        Configuration.browser = browser;
        Configuration.headless = isHeadless;
        Configuration.timeout = timeout;
        Configuration.startMaximized = isMaximized;
        Configuration.reportsFolder = reportsPath;
        Configuration.screenshots = isScreenshotsOnFail;
        Configuration.fastSetValue = isFastSetValue;
        Configuration.clickViaJs = isClickWithJS;
    }


}
