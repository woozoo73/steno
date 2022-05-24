package com.woozooha.steno.replace;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class StenoElementLocatorFactory implements ElementLocatorFactory {

    private final SearchContext searchContext;

    public StenoElementLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public ElementLocator createLocator(Field field) {
        return new StenoElementLocator(this.searchContext, field);
    }

}