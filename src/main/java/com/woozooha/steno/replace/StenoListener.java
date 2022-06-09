package com.woozooha.steno.replace;

import com.woozooha.steno.Steno;
import com.woozooha.steno.model.Event;
import com.woozooha.steno.model.Scene;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
public class StenoListener implements WebDriverListener {

    @Getter
    private final Steno steno;

    public StenoListener(WebDriver driver) {
        steno = new Steno(driver);
    }

    public void beforeAnyCall(Object target, Method method, Object[] args) {
        log.info("beforeAnyCall");
        log.info("target={}, method={}, args={}", target, method, args);

        if (!steno.isListen()) {
            return;
        }

        Event event = Event.of(Event.Type.Before, target, method, args);
        Scene scene = steno.createScene();
        scene.getEvents().add(event);
    }

    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        log.info("afterAnyCall");
        log.info("target={}, method={}, args={}", target, method, args);

        if (!steno.isListen()) {
            return;
        }
        if ("quit".equals(method.getName())) {
            return;
        }

        Event event = Event.of(Event.Type.After, target, method, args);
        Scene scene = steno.getStory().lastScene();
        scene.getEvents().add(event);
        steno.saveScene();
    }

    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        log.info("onError");
    }

    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
        log.info("beforeAnyWebDriverCall");
    }

    public void afterAnyWebDriverCall(WebDriver driver, Method method, Object[] args, Object result) {
        log.info("afterAnyWebDriverCall");
    }

    public void beforeGet(WebDriver driver, String url) {
        log.info("beforeGet");
    }

    public void afterGet(WebDriver driver, String url) {
        log.info("afterGet");
    }

    public void beforeGetCurrentUrl(WebDriver driver) {
        log.info("beforeGetCurrentUrl");
    }

    public void afterGetCurrentUrl(String result, WebDriver driver) {
        log.info("afterGetCurrentUrl");
    }

    public void beforeGetTitle(WebDriver driver) {
        log.info("beforeGetTitle");
    }

    public void afterGetTitle(WebDriver driver, String result) {
        log.info("afterGetTitle");
    }

    public void beforeFindElement(WebDriver driver, By locator) {
        log.info("beforeFindElement");
    }

    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        log.info("afterFindElement");
    }

    public void beforeFindElements(WebDriver driver, By locator) {
        log.info("beforeFindElements");
    }

    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        log.info("afterFindElements");
    }

    public void beforeGetPageSource(WebDriver driver) {
        log.info("beforeGetPageSource");
    }

    public void afterGetPageSource(WebDriver driver, String result) {
        log.info("afterGetPageSource");
    }

    public void beforeClose(WebDriver driver) {
        log.info("beforeClose");
    }

    public void afterClose(WebDriver driver) {
        log.info("afterClose");
    }

    public void beforeQuit(WebDriver driver) {
        log.info("beforeQuit");
    }

    public void afterQuit(WebDriver driver) {
        log.info("afterQuit");
    }

    public void beforeGetWindowHandles(WebDriver driver) {
        log.info("beforeGetWindowHandles");
    }

    public void afterGetWindowHandles(WebDriver driver, Set<String> result) {
        log.info("afterGetWindowHandles");
    }

    public void beforeGetWindowHandle(WebDriver driver) {
        log.info("beforeGetWindowHandle");
    }

    public void afterGetWindowHandle(WebDriver driver, String result) {
        log.info("afterGetWindowHandle");
    }

    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        log.info("beforeExecuteScript");
    }

    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        log.info("afterExecuteScript");
    }

    public void beforeExecuteAsyncScript(WebDriver driver, String script, Object[] args) {
        log.info("beforeExecuteAsyncScript");
    }

    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
        log.info("afterExecuteAsyncScript");
    }

    public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
        log.info("beforePerform");
    }

    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        log.info("afterPerform");
    }

    public void beforeResetInputState(WebDriver driver) {
        log.info("beforeResetInputState");
    }

    public void afterResetInputState(WebDriver driver) {
        log.info("afterResetInputState");
    }

    public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
        log.info("beforeAnyWebElementCall");
    }

    public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
        log.info("afterAnyWebElementCall");
    }

    public void beforeClick(WebElement element) {
        log.info("beforeClick");
    }

    public void afterClick(WebElement element) {
        log.info("afterClick");
    }

    public void beforeSubmit(WebElement element) {
        log.info("beforeSubmit");
    }

    public void afterSubmit(WebElement element) {
        log.info("afterSubmit");
    }

    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        log.info("beforeSendKeys");
    }

    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        log.info("afterSendKeys");
    }

    public void beforeClear(WebElement element) {
        log.info("beforeClear");
    }

    public void afterClear(WebElement element) {
        log.info("afterClear");
    }

    public void beforeGetTagName(WebElement element) {
        log.info("beforeGetTagName");
    }

    public void afterGetTagName(WebElement element, String result) {
        log.info("afterGetTagName");
    }

    public void beforeGetAttribute(WebElement element, String name) {
        log.info("beforeGetAttribute");
    }

    public void afterGetAttribute(WebElement element, String name, String result) {
        log.info("afterGetAttribute");
    }

    public void beforeIsSelected(WebElement element) {
        log.info("beforeIsSelected");
    }

    public void afterIsSelected(WebElement element, boolean result) {
        log.info("afterIsSelected");
    }

    public void beforeIsEnabled(WebElement element) {
        log.info("beforeIsEnabled");
    }

    public void afterIsEnabled(WebElement element, boolean result) {
        log.info("afterIsEnabled");
    }

    public void beforeGetText(WebElement element) {
        log.info("beforeGetText");
    }

    public void afterGetText(WebElement element, String result) {
        log.info("afterGetText");
    }

    public void beforeFindElement(WebElement element, By locator) {
        log.info("beforeFindElement");
    }

    public void afterFindElement(WebElement element, By locator, WebElement result) {
        log.info("afterFindElement");
    }

    public void beforeFindElements(WebElement element, By locator) {
        log.info("beforeFindElements");
    }

    public void afterFindElements(WebElement element, By locator, List<WebElement> result) {
        log.info("afterFindElements");
    }

    public void beforeIsDisplayed(WebElement element) {
        log.info("beforeIsDisplayed");
    }

    public void afterIsDisplayed(WebElement element, boolean result) {
        log.info("afterIsDisplayed");
    }

    public void beforeGetLocation(WebElement element) {
        log.info("beforeGetLocation");
    }

    public void afterGetLocation(WebElement element, Point result) {
        log.info("afterGetLocation");
    }

    public void beforeGetSize(WebElement element) {
        log.info("beforeGetSize");
    }

    public void afterGetSize(WebElement element, Dimension result) {
        log.info("afterGetSize");
    }

    public void beforeGetCssValue(WebElement element, String propertyName) {
        log.info("beforeGetCssValue");
    }

    public void afterGetCssValue(WebElement element, String propertyName, String result) {
        log.info("afterGetCssValue");
    }

    public void beforeAnyNavigationCall(WebDriver.Navigation navigation, Method method, Object[] args) {
        log.info("beforeAnyNavigationCall");
    }

    public void afterAnyNavigationCall(WebDriver.Navigation navigation, Method method, Object[] args, Object result) {
        log.info("afterAnyNavigationCall");
    }

    public void beforeTo(WebDriver.Navigation navigation, String url) {
        log.info("beforeTo");
    }

    public void afterTo(WebDriver.Navigation navigation, String url) {
        log.info("afterTo");
    }

    public void beforeTo(WebDriver.Navigation navigation, URL url) {
        log.info("beforeTo");
    }

    public void afterTo(WebDriver.Navigation navigation, URL url) {
        log.info("afterTo");
    }

    public void beforeBack(WebDriver.Navigation navigation) {
        log.info("beforeBack");
    }

    public void afterBack(WebDriver.Navigation navigation) {
        log.info("afterBack");
    }

    public void beforeForward(WebDriver.Navigation navigation) {
        log.info("beforeForward");
    }

    public void afterForward(WebDriver.Navigation navigation) {
        log.info("afterForward");
    }

    public void beforeRefresh(WebDriver.Navigation navigation) {
        log.info("beforeRefresh");
    }

    public void afterRefresh(WebDriver.Navigation navigation) {
        log.info("afterRefresh");
    }

    public void beforeAnyAlertCall(Alert alert, Method method, Object[] args) {
        log.info("beforeAnyAlertCall");
    }

    public void afterAnyAlertCall(Alert alert, Method method, Object[] args, Object result) {
        log.info("afterAnyAlertCall");
    }

    public void beforeAccept(Alert alert) {
        log.info("beforeAccept");
    }

    public void afterAccept(Alert alert) {
        log.info("afterAccept");
    }

    public void beforeDismiss(Alert alert) {
        log.info("beforeDismiss");
    }

    public void afterDismiss(Alert alert) {
        log.info("afterDismiss");
    }

    public void beforeGetText(Alert alert) {
        log.info("beforeGetText");
    }

    public void afterGetText(Alert alert, String result) {
        log.info("afterGetText");
    }

    public void beforeSendKeys(Alert alert, String text) {
        log.info("beforeSendKeys");
    }

    public void afterSendKeys(Alert alert, String text) {
        log.info("afterSendKeys");
    }

    public void beforeAnyOptionsCall(WebDriver.Options options, Method method, Object[] args) {
        log.info("beforeAnyOptionsCall");
    }

    public void afterAnyOptionsCall(WebDriver.Options options, Method method, Object[] args, Object result) {
        log.info("afterAnyOptionsCall");
    }

    public void beforeAddCookie(WebDriver.Options options, Cookie cookie) {
        log.info("beforeAddCookie");
    }

    public void afterAddCookie(WebDriver.Options options, Cookie cookie) {
        log.info("afterAddCookie");
    }

    public void beforeDeleteCookieNamed(WebDriver.Options options, String name) {
        log.info("beforeDeleteCookieNamed");
    }

    public void afterDeleteCookieNamed(WebDriver.Options options, String name) {
        log.info("afterDeleteCookieNamed");
    }

    public void beforeDeleteCookie(WebDriver.Options options, Cookie cookie) {
        log.info("beforeDeleteCookie");
    }

    public void afterDeleteCookie(WebDriver.Options options, Cookie cookie) {
        log.info("afterDeleteCookie");
    }

    public void beforeDeleteAllCookies(WebDriver.Options options) {
        log.info("beforeDeleteAllCookies");
    }

    public void afterDeleteAllCookies(WebDriver.Options options) {
        log.info("afterDeleteAllCookies");
    }

    public void beforeGetCookies(WebDriver.Options options) {
        log.info("beforeGetCookies");
    }

    public void afterGetCookies(WebDriver.Options options, Set<Cookie> result) {
        log.info("afterGetCookies");
    }

    public void beforeGetCookieNamed(WebDriver.Options options, String name) {
        log.info("beforeGetCookieNamed");
    }

    public void afterGetCookieNamed(WebDriver.Options options, String name, Cookie result) {
        log.info("afterGetCookieNamed");
    }

    public void beforeAnyTimeoutsCall(WebDriver.Timeouts timeouts, Method method, Object[] args) {
        log.info("beforeAnyTimeoutsCall");
    }

    public void afterAnyTimeoutsCall(WebDriver.Timeouts timeouts, Method method, Object[] args, Object result) {
        log.info("afterAnyTimeoutsCall");
    }

    public void beforeImplicitlyWait(WebDriver.Timeouts timeouts, Duration duration) {
        log.info("beforeImplicitlyWait");
    }

    public void afterImplicitlyWait(WebDriver.Timeouts timeouts, Duration duration) {
        log.info("afterImplicitlyWait");
    }

    public void beforeSetScriptTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        log.info("beforeSetScriptTimeout");
    }

    public void afterSetScriptTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        log.info("afterSetScriptTimeout");
    }

    public void beforePageLoadTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        log.info("beforePageLoadTimeout");
    }

    public void afterPageLoadTimeout(WebDriver.Timeouts timeouts, Duration duration) {
        log.info("afterPageLoadTimeout");
    }

    public void beforeAnyWindowCall(WebDriver.Window window, Method method, Object[] args) {
        log.info("beforeAnyWindowCall");
    }

    public void afterAnyWindowCall(WebDriver.Window window, Method method, Object[] args, Object result) {
        log.info("afterAnyWindowCall");
    }

    public void beforeGetSize(WebDriver.Window window) {
        log.info("beforeGetSize");
    }

    public void afterGetSize(WebDriver.Window window, Dimension result) {
        log.info("afterGetSize");
    }

    public void beforeSetSize(WebDriver.Window window, Dimension size) {
        log.info("beforeSetSize");
    }

    public void afterSetSize(WebDriver.Window window, Dimension size) {
        log.info("afterSetSize");
    }

    public void beforeGetPosition(WebDriver.Window window) {
        log.info("beforeGetPosition");
    }

    public void afterGetPosition(WebDriver.Window window, Point result) {
        log.info("afterGetPosition");
    }

    public void beforeSetPosition(WebDriver.Window window, Point position) {
        log.info("beforeSetPosition");
    }

    public void afterSetPosition(WebDriver.Window window, Point position) {
        log.info("afterSetPosition");
    }

    public void beforeMaximize(WebDriver.Window window) {
        log.info("beforeMaximize");
    }

    public void afterMaximize(WebDriver.Window window) {
        log.info("afterMaximize");
    }

    public void beforeFullscreen(WebDriver.Window window) {
        log.info("beforeFullscreen");
    }

    public void afterFullscreen(WebDriver.Window window) {
        log.info("afterFullscreen");
    }

}
