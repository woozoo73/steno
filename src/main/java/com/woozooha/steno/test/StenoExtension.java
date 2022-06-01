package com.woozooha.steno.test;

import com.woozooha.steno.Steno;
import com.woozooha.steno.replace.StenoListener;
import com.woozooha.steno.util.ContextUtils;
import com.woozooha.steno.util.ReflectionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class StenoExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        log.info("beforeAll extensionContext={}", extensionContext);

        StenoTest stenoTest = readStenoTest(extensionContext);
        if (stenoTest == null) {
            return;
        }

        ContextUtils.replacePageFactory();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        log.info("afterAll extensionContext={}", extensionContext);

        StenoTest stenoTest = readStenoTest(extensionContext);
        if (stenoTest == null) {
            return;
        }

        ContextUtils.resetPageFactory();
    }

    @Override
    @SneakyThrows
    public void beforeEach(ExtensionContext extensionContext) {
        log.info("beforeEach extensionContext={}", extensionContext);

        StenoTest stenoTest = readStenoTest(extensionContext);
        if (stenoTest == null) {
            return;
        }

        WebDriver driver = createAndBindDriver(extensionContext);

        Steno.start(driver);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        log.info("afterEach extensionContext={}", extensionContext);

        StenoTest stenoTest = readStenoTest(extensionContext);
        if (stenoTest == null) {
            return;
        }

        Steno.end();
    }

    protected StenoTest readStenoTest(ExtensionContext extensionContext) {
        Class<?> clazz = extensionContext.getTestClass().orElse(null);
        if (clazz == null) {
            return null;
        }

        return readStenoTest(clazz);
    }

    protected StenoTest readStenoTest(Class<?> clazz) {
        StenoTest stenoTest = clazz.getAnnotation(StenoTest.class);
        if (stenoTest == null) {
            return null;
        }

        return stenoTest;
    }

    protected Object readTarget(ExtensionContext extensionContext) {
        return extensionContext.getTestInstance().orElse(null);
    }

    protected WebDriver createAndBindDriver(ExtensionContext extensionContext) {
        Object target = readTarget(extensionContext);

        WebDriver driver = createWebDriver(target);
        WebDriver decorated = addStenoListener(driver);
        bindWebDriver(target, decorated);

        return decorated;
    }

    @SneakyThrows
    protected WebDriver createWebDriver(Object target) {
        WebDriver driver = createByStenoWebDriver(target);
        if (driver == null) {
            driver = createByStenoWebDriverFactory(target);
        }
        if (driver == null) {
            driver = createByReturnWebDriverMethod(target);
        }

        return driver;
    }

    @SneakyThrows
    protected WebDriver createByStenoWebDriver(Object target) {
        List<Method> webDriverMethods = ReflectionUtils.getMethodsAnnotatedWith(target.getClass(), StenoWebDriver.class);
        if (webDriverMethods.isEmpty()) {
            return null;
        }
        if (webDriverMethods.size() > 1) {
            throw new RuntimeException("Only 1 annotated " + StenoWebDriver.class + " method needed. but, was found " + webDriverMethods.size());
        }

        Method webDriverMethod = webDriverMethods.get(0);
        webDriverMethod.setAccessible(true);
        WebDriver driver = (WebDriver) webDriverMethod.invoke(target);

        return driver;
    }

    @SneakyThrows
    protected WebDriver createByStenoWebDriverFactory(Object target) {
        StenoWebDriverFactory stenoWebDriverFactory = target.getClass().getAnnotation(StenoWebDriverFactory.class);
        if (stenoWebDriverFactory == null) {
            return null;
        }

        Class<? extends WebDriverFactory> factoryClass = stenoWebDriverFactory.value();
        WebDriverFactory factory = factoryClass.newInstance();

        WebDriver driver = factory.create();

        return driver;
    }

    @SneakyThrows
    protected WebDriver createByReturnWebDriverMethod(Object target) {
        List<Method> webDriverMethods = ReflectionUtils.getMethodsReturnWith(target.getClass(), WebDriver.class);
        if (webDriverMethods.isEmpty()) {
            return null;
        }
        if (webDriverMethods.size() > 1) {
            throw new RuntimeException("Only 1 return " + WebDriver.class + " method needed. but, was found " + webDriverMethods.size());
        }

        Method webDriverMethod = webDriverMethods.get(0);
        webDriverMethod.setAccessible(true);
        WebDriver driver = (WebDriver) webDriverMethod.invoke(target);

        return driver;
    }

    @SneakyThrows
    protected boolean bindWebDriver(Object target, WebDriver driver) {
        boolean bound = bindToStenoWebDriver(target, driver);
        if (!bound) {
            bound = bindToWebDriverTypeField(target, driver);
        }

        return bound;
    }

    @SneakyThrows
    protected boolean bindToStenoWebDriver(Object target, WebDriver driver) {
        List<Field> webDriverFields = ReflectionUtils.getFieldsAnnotatedWith(target.getClass(), StenoWebDriver.class);
        if (webDriverFields.isEmpty()) {
            return false;
        }
        if (webDriverFields.size() > 1) {
            throw new RuntimeException("Only 1 annotated " + StenoWebDriver.class + " field needed. but, was found " + webDriverFields.size());
        }

        Field webDriverField = webDriverFields.get(0);
        webDriverField.setAccessible(true);
        webDriverField.set(target, driver);

        return true;
    }

    @SneakyThrows
    protected boolean bindToWebDriverTypeField(Object target, WebDriver driver) {
        List<Field> webDriverFields = ReflectionUtils.getFieldsTypeWith(target.getClass(), WebDriver.class);
        if (webDriverFields.isEmpty()) {
            return false;
        }
        if (webDriverFields.size() > 1) {
            throw new RuntimeException("Only 1 type " + WebDriver.class + " field needed. but, was found " + webDriverFields.size());
        }

        Field webDriverField = webDriverFields.get(0);
        webDriverField.setAccessible(true);
        webDriverField.set(target, driver);

        return true;
    }

    protected WebDriver addStenoListener(WebDriver driver) {
        StenoListener listener = new StenoListener();
        WebDriver decorated = new EventFiringDecorator(listener).decorate(driver);

        return decorated;
    }

}
