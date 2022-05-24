package com.woozooha.steno.replace;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

public class StenoFieldDecorator extends DefaultFieldDecorator {

    public StenoFieldDecorator(ElementLocatorFactory factory) {
        super(factory);
    }

    @Override
    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new StenoLocatingElementHandler(locator);
        WebElement proxy = (WebElement) Proxy.newProxyInstance(loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler);
        return proxy;
    }

    @Override
    protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new StenoLocatingElementListHandler(locator);
        List<WebElement> proxy = (List) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
        return proxy;
    }

}
