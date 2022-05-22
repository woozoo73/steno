package com.woozooha.steno;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class IndexPage {

    WebDriver driver;

    @FindBy(id = "searchInput")
    WebElement searchInput;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

}
