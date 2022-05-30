package com.woozooha.steno;

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
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

@ExtendWith(StenoExtension.class)
@StenoTest
@Slf4j
class StenoRunTest {

    @StenoWebDriver
    WebDriver driver;

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
    }

    @StenoWebDriver
    public WebDriver makeWebDriver() {
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().window().maximize();

        return driver;
    }

    @Test
    void get() {
        IndexPage indexPage = PageFactory.initElements(driver, IndexPage.class);
        indexPage.index();
        ResultPage resultPage = indexPage.search("Selenium (software)");

        String firstHeading = resultPage.getFirstHeading();
        String siteSub = resultPage.getSiteSub();

        log.info("firstHeading={}", firstHeading);
        log.info("siteSub={}", siteSub);
    }

}
