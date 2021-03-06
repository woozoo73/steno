package com.woozooha.steno.example.wikipedia;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

@Getter
public class IndexPage {

    final WebDriver driver;

    @FindBy(id = "searchInput")
    WebElement searchInput;

    @FindBy(id = "searchLanguage")
    @CacheLookup
    WebElement searchLanguage;

    @FindBy(className = "pure-button")
    WebElement searchButton;

    public IndexPage(WebDriver driver) {
        this.driver = driver;
    }

    public void index() {
        driver.get("https://www.wikipedia.org");
    }

    public ResultPage search(String input) {
        searchInput.sendKeys(input);

        Select languages = new Select(searchLanguage);
        languages.selectByVisibleText("English");

        searchButton.submit();

        return PageFactory.initElements(driver, ResultPage.class);
    }

}
