package com.woozooha.steno;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringDecorator;

@Slf4j
class PageListenerTest {

    WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        Steno steno = new Steno();
    }

    @BeforeEach
    void beforeEach() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver");
        WebDriver origin = new ChromeDriver();
        PageListener listener = new PageListener();
        driver = new EventFiringDecorator(listener).decorate(origin);
    }

    @AfterEach
    void afterEach() {
        driver.quit();
    }

    @Test
    void get() {
        driver.get("https://www.wikipedia.org");
        IndexPage indexPage = PageFactory.initElements(driver, IndexPage.class);

        log.info("IndexPage={}", indexPage);
    }

}
