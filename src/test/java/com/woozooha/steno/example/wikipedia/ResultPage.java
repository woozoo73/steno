package com.woozooha.steno.example.wikipedia;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class ResultPage {

    final WebDriver driver;

    @FindBy(id = "firstHeading")
    WebElement firstHeading;

    @FindBy(id = "siteSub")
    WebElement siteSub;

    public ResultPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getFirstHeading() {
        return firstHeading.getText();
    }

    public String getSiteSub() {
        return siteSub.getText();
    }

}
