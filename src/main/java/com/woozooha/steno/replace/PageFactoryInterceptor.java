package com.woozooha.steno.replace;

import com.woozooha.steno.Steno;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

@Slf4j
public class PageFactoryInterceptor {

    public static void initElements(SearchContext searchContext, Object page) {
        StenoDriver stenoDriver = (StenoDriver) searchContext;
        Steno steno = stenoDriver.getSteno();

        beforeInitElements(steno, page);

        PageFactory.initElements((ElementLocatorFactory) (new StenoElementLocatorFactory(steno, searchContext)), (Object) page);

        afterInitElements(steno, page);
    }

    public static void beforeInitElements(Steno steno, Object page) {
        steno.createPage(page);
        steno.createScene();
    }

    public static void afterInitElements(Steno steno, Object page) {
        steno.saveScene();
    }

}
