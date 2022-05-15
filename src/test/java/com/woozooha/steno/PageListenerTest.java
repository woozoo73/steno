package com.woozooha.steno;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

class PageListenerTest {

    WebDriver driver;

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
    }

}
