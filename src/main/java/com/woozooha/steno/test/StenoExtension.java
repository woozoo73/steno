package com.woozooha.steno.test;

import com.woozooha.steno.Steno;
import com.woozooha.steno.replace.StenoDriver;
import com.woozooha.steno.replace.StenoListener;
import com.woozooha.steno.util.ContextUtils;
import com.woozooha.steno.util.ReflectionUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

@Slf4j
public class StenoExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private String groupId;

    private Steno steno;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        log.info("beforeAll extensionContext={}", extensionContext);

        StenoTest stenoTest = readStenoTest(extensionContext);
        if (stenoTest == null) {
            return;
        }

        ContextUtils.replacePageFactory();
        groupId = generateGroupId();
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

        steno = createAndBindWebDriver(extensionContext);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        log.info("afterEach extensionContext={}", extensionContext);

        StenoTest stenoTest = readStenoTest(extensionContext);
        if (stenoTest == null) {
            return;
        }

        steno.saveStory();
    }

    protected StenoTest readStenoTest(ExtensionContext extensionContext) {
        Class<?> clazz = extensionContext.getTestClass().orElse(null);
        if (clazz == null) {
            return null;
        }

        return readStenoTest(clazz);
    }

    protected StenoTest readStenoTest(Class<?> clazz) {
        return clazz.getAnnotation(StenoTest.class);
    }

    protected Object readTarget(ExtensionContext extensionContext) {
        return extensionContext.getTestInstance().orElse(null);
    }

    protected Steno createAndBindWebDriver(ExtensionContext extensionContext) {
        Object target = readTarget(extensionContext);

        WebDriver driver = createWebDriver(target);
        Class<?> targetClass = target.getClass();
        Method targetMethod = extensionContext.getTestMethod().orElse(null);
        StenoListener listener = new StenoListener(driver, groupId, targetClass, targetMethod);
        WebDriver decorated = addStenoListener(driver, listener);
        StenoDriver stenoDriver = new StenoDriver(listener.getSteno(), decorated);
        bindWebDriver(target, stenoDriver);

        return listener.getSteno();
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

        return (WebDriver) webDriverMethod.invoke(target);
    }

    @SneakyThrows
    protected WebDriver createByStenoWebDriverFactory(Object target) {
        StenoWebDriverFactory stenoWebDriverFactory = target.getClass().getAnnotation(StenoWebDriverFactory.class);
        if (stenoWebDriverFactory == null) {
            return null;
        }

        Class<? extends WebDriverFactory> factoryClass = stenoWebDriverFactory.value();
        WebDriverFactory factory = factoryClass.getConstructor().newInstance();

        return factory.create();
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

        return (WebDriver) webDriverMethod.invoke(target);
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

    protected WebDriver addStenoListener(WebDriver driver, WebDriverListener listener) {
        return new EventFiringDecorator(listener).decorate(driver);
    }

    public String generateGroupId() {
        return pseudoUuid();
    }

    protected String pseudoUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
