package com.woozooha.steno.example.wikipedia;

import com.woozooha.steno.test.StenoExtension;
import com.woozooha.steno.test.StenoTest;
import com.woozooha.steno.test.StenoWebDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

@ExtendWith(StenoExtension.class)
@StenoTest
@Slf4j
class StenoTest5 {

    String keyword = "Selenium (software)";

    WebDriver driver;

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
    }

    public String getKeyword() {
        return keyword;
    }

    @StenoWebDriver
    public WebDriver createDriver() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().window().maximize();

        return driver;
    }

    @Test
    void search() {
        IndexPage indexPage = PageFactory.initElements(driver, IndexPage.class);
        indexPage.index();
        ResultPage resultPage = indexPage.search(keyword);

        String firstHeading = resultPage.getFirstHeading();
        String siteSub = resultPage.getSiteSub();
        List<String> tocs = resultPage.getTocs();

        log.info("firstHeading={}", firstHeading);
        log.info("siteSub={}", siteSub);
        log.info("tocs={}", tocs);
    }

}
