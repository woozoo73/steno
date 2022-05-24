package com.woozooha.steno;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.time.Duration;

@Slf4j
class StenoTest {

    WebDriver driver;

    @BeforeEach
    void beforeEach() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver");
        WebDriver origin = new ChromeDriver();
        StenoListener listener = new StenoListener();
        driver = new EventFiringDecorator(listener).decorate(origin);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().window().maximize();

        Steno.start(driver);
    }

    @AfterEach
    void afterEach() {
        Steno.stop(driver);

        driver.quit();
    }

    @Test
    void get() {
        driver.get("https://www.wikipedia.org");
        IndexPage indexPage = PageFactory.initElements(driver, IndexPage.class);

        indexPage.getSearchInput().sendKeys("abcd");

        log.info("IndexPage={}", indexPage);
    }

}
