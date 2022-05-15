package com.woozooha.steno;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

@Slf4j
public class PageListener implements WebDriverListener {

    @Override
    public void beforeGet(WebDriver driver, String url) {
        log.info("beforeGet: {}, {}", driver, url);
        System.out.println("url=" + url);
    }

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        WebElement webElement = driver.findElement(locator);
        webElement.getRect();
    }

}
