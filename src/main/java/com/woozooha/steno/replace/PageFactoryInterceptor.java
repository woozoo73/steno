package com.woozooha.steno.replace;

import com.woozooha.steno.util.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

@Slf4j
public class PageFactoryInterceptor {

    public static void initElements(SearchContext searchContext, Object page) {
        beforeInitElements(page);

        PageFactory.initElements((ElementLocatorFactory) (new StenoElementLocatorFactory(searchContext)), (Object) page);

        afterInitElements(page);
    }

    public static void beforeInitElements(Object page) {
        ContextUtils.createScene(page);
        ContextUtils.saveScene();
    }

    public static void afterInitElements(Object page) {
        // do nothing.
    }

}
