package com.woozooha.steno.replace;

import com.woozooha.steno.Steno;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Field;
import java.util.List;

public class StenoElementLocator extends DefaultElementLocator {

    private final Steno steno;

    private final String fieldName;

    private final By by;

    public StenoElementLocator(Steno steno, SearchContext searchContext, Field field) {
        this(steno, searchContext, field, (AbstractAnnotations) (new Annotations(field)));
    }

    public StenoElementLocator(Steno steno, SearchContext searchContext, Field field, AbstractAnnotations annotations) {
        super(searchContext, annotations);
        this.steno = steno;
        this.fieldName = field.getName();
        this.by = annotations.buildBy();
    }

    public WebElement findElement() {
        WebElement webElement = super.findElement();

        steno.addElement(webElement, by, fieldName);

        return webElement;
    }

    public List<WebElement> findElements() {
        List<WebElement> webElements = super.findElements();

        for (int i = 0; i < webElements.size(); i++) {
            WebElement webElement = webElements.get(i);

            steno.addElement(webElement, by, fieldName + "[" + i + "]");
        }

        return webElements;
    }

}
