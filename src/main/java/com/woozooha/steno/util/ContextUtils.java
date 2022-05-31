package com.woozooha.steno.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.woozooha.steno.Steno;
import com.woozooha.steno.model.Element;
import com.woozooha.steno.model.Scene;
import com.woozooha.steno.model.Story;
import com.woozooha.steno.replace.PageFactoryInterceptor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

@Slf4j
public abstract class ContextUtils {

    @Getter
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static void replacePageFactory() {
        try {
            ByteBuddyAgent.install();
            ByteBuddy byteBuddy = new ByteBuddy();
            byteBuddy
                    .redefine(PageFactory.class)
                    .method(named("initElements").and(takesArguments(SearchContext.class, Object.class)))
                    .intercept(MethodDelegation.to(PageFactoryInterceptor.class))
                    .make()
                    .load(
                            PageFactory.class.getClassLoader(),
                            ClassReloadingStrategy.fromInstalledAgent());
        } catch (Throwable t) {
            // ignore.
            log.error("Exception occurred.", t);
        }
    }

    @SneakyThrows
    public static void resetPageFactory() {
        try {
            ClassReloadingStrategy.fromInstalledAgent().reset(PageFactory.class);
        } catch (Throwable t) {
            // ignore.
            log.error("Exception occurred.", t);
        }
    }

    public static Steno currentSteno() {
        return Steno.currentSteno();
    }

    public static Story currentStory() {
        Steno steno = currentSteno();
        if (steno == null) {
            return null;
        }

        return steno.getStory();
    }

    public static Scene currentScene() {
        Story story = currentStory();
        if (story == null) {
            return null;
        }

        return story.lastScene();
    }

    public static WebDriver currentDriver() {
        Steno steno = Steno.currentSteno();
        if (steno == null) {
            return null;
        }

        return steno.getDriver();
    }

    public static Scene createScene(Object page) {
        Story story = currentStory();
        if (story == null) {
            return null;
        }

        Scene scene = story.createScene();
        scene.setPageClass(page.getClass());
        WebDriver driver = currentDriver();
        scene.setUrl(driver.getCurrentUrl());
        scene.setTitle(driver.getTitle());

        return scene;
    }

    public static Element addElement(WebElement webElement) {
        return addElement(webElement, null);
    }

    public static Element addElement(WebElement webElement, By by) {
        return addElement(webElement, by, null);
    }

    public static Element addElement(WebElement webElement, By by, String fieldName) {
        if (webElement == null) {
            return null;
        }

        Scene scene = currentScene();
        if (scene == null) {
            return null;
        }

        if (fieldName != null) {
            boolean alreadyAdded = scene.getElements().stream()
                    .filter(e -> fieldName.equals(e.getFieldName()))
                    .map(e -> Boolean.TRUE).findFirst().orElse(Boolean.FALSE);
            if (alreadyAdded) {
                return null;
            }
        }

        Element element = new Element();
        element.setRect(Element.Rect.of(webElement.getRect()));
        if (by != null) {
            element.setBy(by.toString());
        }
        if (fieldName != null) {
            element.setFieldName(fieldName);
        }

        scene.getElements().add(element);

        return element;
    }

    public static void saveStory() {
        saveStoryData();
    }

    public static void saveScene() {
        try {
            Steno.listen(false);

            saveSceneData();
            saveScreenshot();
            saveSource();

            Story story = currentStory();
            story.setSceneId(story.getSceneId() + 1L);
            Scene scene = currentScene();
            scene.setId(story.getSceneId());
        } finally {
            Steno.listen(true);
        }
    }

    @SneakyThrows
    private static void saveStoryData() {
        Story story = currentStory();
        String json = Steno.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(story);
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, story.getDataFilename());
        Files.write(json.getBytes(StandardCharsets.UTF_8), to);
    }

    @SneakyThrows
    private static void saveSceneData() {
        Story story = currentStory();
        Scene scene = currentScene();

        String json = Steno.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(scene);
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, scene.getDataFilename());
        Files.write(json.getBytes(StandardCharsets.UTF_8), to);
    }

    @SneakyThrows
    private static void saveScreenshot() {
        Story story = currentStory();
        Scene scene = currentScene();

        File from = ((TakesScreenshot) currentDriver()).getScreenshotAs(OutputType.FILE);
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, scene.getScreenshotFilename());
        Files.copy(from, to);
    }

    @SneakyThrows
    private static void saveSource() {
        Story story = currentStory();
        Scene scene = currentScene();

        String source = currentDriver().getPageSource();
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, scene.getPageSourceFilename());
        Files.write(source.getBytes(StandardCharsets.UTF_8), to);
    }

}
