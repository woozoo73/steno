package com.woozooha.steno.example.wikipedia;

import com.woozooha.steno.example.common.ChromeFactory;
import com.woozooha.steno.test.StenoExtension;
import com.woozooha.steno.test.StenoTest;
import com.woozooha.steno.test.StenoWebDriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@ExtendWith(StenoExtension.class)
@StenoTest
@StenoWebDriverFactory(ChromeFactory.class)
@Slf4j
class StenoTest1 {

    WebDriver driver;

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
    }

    @Test
    void get() {
        IndexPage indexPage = PageFactory.initElements(driver, IndexPage.class);
        indexPage.index();
        ResultPage resultPage = indexPage.search("Selenium (software)");

        String firstHeading = resultPage.getFirstHeading();
        String siteSub = resultPage.getSiteSub();
        String tocs0 = resultPage.getTocs(0);

        log.info("firstHeading={}", firstHeading);
        log.info("siteSub={}", siteSub);
        log.info("tocs[0]={}", tocs0);
    }

}
