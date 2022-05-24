package com.woozooha.steno.replace;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Field;
import java.util.List;

public class StenoElementLocator extends DefaultElementLocator {

    public StenoElementLocator(SearchContext searchContext, Field field) {
        super(searchContext, field);
    }

    public StenoElementLocator(SearchContext searchContext, AbstractAnnotations annotations) {
        super(searchContext, annotations);
    }

    public WebElement findElement() {
        WebElement webElement = super.findElement();

        return webElement;
    }

    public List<WebElement> findElements() {
        List<WebElement> webElements = super.findElements();

        return webElements;
    }

}
