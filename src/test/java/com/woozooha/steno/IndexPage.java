package com.woozooha.steno;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class IndexPage {

    WebDriver driver;

    @FindBy(id = "searchInput")
    WebElement searchInput;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

}
