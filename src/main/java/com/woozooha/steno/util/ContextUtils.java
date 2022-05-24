package com.woozooha.steno.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.woozooha.steno.Element;
import com.woozooha.steno.Scene;
import com.woozooha.steno.Steno;
import com.woozooha.steno.Story;
import lombok.Getter;
import lombok.SneakyThrows;
import org.openqa.selenium.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

public abstract class ContextUtils {

    @Getter
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
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
        if (webElement == null) {
            return null;
        }

        Scene scene = currentScene();
        if (scene == null) {
            return null;
        }

        Element element = new Element();
        element.setRect(webElement.getRect());
        element.setBy(by);

        scene.getElements().add(element);

        return element;
    }

    public static void saveStory() {
        saveStoryData();
    }

    public static void saveScene() {
        Steno.listen(false);

        try {
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
