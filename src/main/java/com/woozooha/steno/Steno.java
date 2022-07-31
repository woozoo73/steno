package com.woozooha.steno;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woozooha.steno.conf.Config;
import com.woozooha.steno.model.Element;
import com.woozooha.steno.model.Page;
import com.woozooha.steno.model.Scene;
import com.woozooha.steno.model.Story;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.function.Supplier;

@Slf4j
public class Steno {

    @Getter
    @Setter
    private String groupId;

    @Getter
    @Setter
    private Class<?> targetClass;

    @Getter
    @Setter
    private Method targetMethod;

    @Getter
    @Setter
    private boolean listen = true;

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    private final Story story;

    @Getter
    private final WebDriver driver;

    @Getter
    private final Config config;

    public Steno(WebDriver driver, String groupId, Class<?> targetClass, Method targetMethod) {
        this.driver = driver;
        this.groupId = groupId;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.story = new Story(groupId, targetClass, targetMethod);
        this.config = Config.getCurrent();
    }

    public Page createPage(Object target) {
        return doQuietly(() -> {
            Page page = new Page();
            page.setPageClass(target.getClass());
            page.setUrl(driver.getCurrentUrl());
            page.setTitle(driver.getTitle());

            story.getPages().add(page);

            return page;
        });
    }

    public Scene createScene() {
        Page page = story.lastPage();

        return doQuietly(() -> {
            Scene scene = new Scene(story.getScenes().size());
            if (page != null) {
                scene.setPageClass(page.getPageClass());
            }
            scene.setUrl(driver.getCurrentUrl());
            scene.setTitle(driver.getTitle());

            story.getScenes().add(scene);

            return scene;
        });
    }

    public Element addElement(WebElement webElement, By by, String fieldName) {
        return doQuietly(() -> {
            if (webElement == null) {
                return null;
            }

            Element element = new Element();
            element.setRect(Element.Rect.of(webElement.getRect()));
            if (by != null) {
                element.setBy(by.toString());
            }
            if (fieldName != null) {
                element.setFieldName(fieldName);
            }

            Page page = story.lastPage();
            if (page != null) {
                if (fieldName != null) {
                    boolean alreadyAdded = page.getElements().stream()
                            .filter(e -> fieldName.equals(e.getFieldName()))
                            .map(e -> Boolean.TRUE).findFirst().orElse(Boolean.FALSE);
                    if (alreadyAdded) {
                        return element;
                    }
                }

                page.getElements().add(element);
            }

            Scene scene = story.lastScene();
            if (scene != null) {
                scene.getElements().add(element);
            }

            return element;
        });
    }

    public void saveScene() {
        doQuietly(() -> {
            saveSceneData();
            saveScreenshot();
            saveSource();
            return null;
        });
    }

    public <T> T doQuietly(Supplier<T> s) {
        try {
            setListen(false);

            return s.get();
        } finally {
            setListen(true);
        }
    }

    public void saveStory() {
        config.getHistory().write(story);
    }

    private void saveSceneData() {
        Scene scene = story.lastScene();

        config.getHistory().write(story, scene);
    }

    private void saveScreenshot() {
        Scene scene = story.lastScene();
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        config.getHistory().write(story, scene, screenshot);
    }

    private void saveSource() {
        Scene scene = story.lastScene();
        String source = driver.getPageSource();

        config.getHistory().write(story, scene, source);
    }

}
