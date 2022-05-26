package com.woozooha.steno.replace;

import com.woozooha.steno.Steno;
import com.woozooha.steno.util.ContextUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

import java.lang.reflect.Field;
import java.util.List;

public class StenoElementLocator extends DefaultElementLocator {

    private final String fieldName;

    private final By by;

    public StenoElementLocator(SearchContext searchContext, Field field) {
        this(searchContext, field, (AbstractAnnotations) (new Annotations(field)));
    }

    public StenoElementLocator(SearchContext searchContext, Field field, AbstractAnnotations annotations) {
        super(searchContext, annotations);
        this.fieldName = field.getName();
        this.by = annotations.buildBy();
    }

    public WebElement findElement() {
        try {
            Steno.listen(false);
            WebElement webElement = super.findElement();

            ContextUtils.addElement(webElement, by, fieldName);

            return webElement;
        } finally {
            Steno.listen(true);
        }
    }

    public List<WebElement> findElements() {
        try {
            Steno.listen(false);
            List<WebElement> webElements = super.findElements();

            for (int i = 0; i < webElements.size(); i++) {
                WebElement webElement = webElements.get(i);
                ContextUtils.addElement(webElement, by, fieldName + "[" + i + "]");
            }

            return webElements;
        } finally {
            Steno.listen(true);
        }
    }

}
