package com.woozooha.steno.replace;

import com.woozooha.steno.util.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

@Slf4j
public class PageFactoryInterceptor {

    public static void initElements(ElementLocatorFactory factory, Object page) {
        beforeInitElements(page);

        PageFactory.initElements((FieldDecorator) (new DefaultFieldDecorator(factory)), (Object) page);

        afterInitElements(page);
    }

    public static void beforeInitElements(Object page) {
        ContextUtils.createScene(page);
    }

    public static void afterInitElements(Object page) {
        // do nothing.
    }

}
