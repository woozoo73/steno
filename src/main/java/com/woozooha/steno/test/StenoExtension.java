package com.woozooha.steno.test;

import com.woozooha.steno.Steno;
import com.woozooha.steno.replace.StenoListener;
import com.woozooha.steno.util.ContextUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
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

        WebDriver driver = loadAndBindWebDriver(extensionContext);

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

    protected WebDriver loadAndBindWebDriver(ExtensionContext extensionContext) throws IllegalAccessException, InvocationTargetException {
        Object target = readTarget(extensionContext);

        List<Method> webDriverMethods = getMethodsAnnotatedWith(target.getClass(), StenoWebDriver.class);
        if (webDriverMethods.isEmpty()) {
            throw new RuntimeException("No annotated " + StenoWebDriver.class + " method found.");
        }
        if (webDriverMethods.size() > 1) {
            throw new RuntimeException("Only 1 annotated " + StenoWebDriver.class + " method needed. but, was found " + webDriverMethods.size());
        }

        Method webDriverMethod = webDriverMethods.get(0);
        webDriverMethod.setAccessible(true);
        WebDriver driver = (WebDriver) webDriverMethod.invoke(target);

        List<Field> webDriverFields = getFieldsAnnotatedWith(target.getClass(), StenoWebDriver.class);
        if (webDriverFields.isEmpty()) {
            throw new RuntimeException("No annotated " + StenoWebDriver.class + " field found.");
        }
        if (webDriverFields.size() > 1) {
            throw new RuntimeException("Only 1 annotated " + StenoWebDriver.class + " field needed. but, was found " + webDriverMethods.size());
        }

        StenoListener listener = new StenoListener();
        WebDriver decorated = new EventFiringDecorator(listener).decorate(driver);
        decorated.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        decorated.manage().window().maximize();

        Field webDriverField = webDriverFields.get(0);
        webDriverField.setAccessible(true);
        webDriverField.set(target, decorated);

        return decorated;
    }

    protected static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            // Exit if candidates exist.
            if (!methods.isEmpty()) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return methods;
    }

    protected static List<Field> getFieldsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Field> fields = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (final Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotation)) {
                    fields.add(field);
                }
            }
            // Exit if candidates exist.
            if (!fields.isEmpty()) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

}
