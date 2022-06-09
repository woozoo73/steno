package com.woozooha.steno.replace;

import com.woozooha.steno.Steno;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class StenoElementLocatorFactory implements ElementLocatorFactory {

    private final Steno steno;

    private final SearchContext searchContext;

    public StenoElementLocatorFactory(Steno steno, SearchContext searchContext) {
        this.steno = steno;
        this.searchContext = searchContext;
    }

    public ElementLocator createLocator(Field field) {
        return new StenoElementLocator(steno, searchContext, field);
    }

}
