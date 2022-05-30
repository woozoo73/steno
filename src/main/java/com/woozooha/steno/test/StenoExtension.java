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
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StenoExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        log.info("beforeAll extensionContext={}", extensionContext);

        ContextUtils.replacePageFactory();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        log.info("afterAll extensionContext={}", extensionContext);

        ContextUtils.resetPageFactory();
    }

    @Override
    @SneakyThrows
    public void beforeEach(ExtensionContext extensionContext) {
        log.info("beforeEach extensionContext={}", extensionContext);

        Object target = extensionContext.getTestInstance().orElse(null);
        if (target == null) {
            return;
        }

        StenoTest stenoTest = target.getClass().getAnnotation(StenoTest.class);
        if (stenoTest == null) {
            return;
        }

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

        Steno.start(decorated);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        log.info("afterEach extensionContext={}", extensionContext);

        Steno.end();
    }

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
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

    public static List<Field> getFieldsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
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
