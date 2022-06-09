package com.woozooha.steno;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.woozooha.steno.model.Page;
import com.woozooha.steno.model.Scene;
import com.woozooha.steno.model.Story;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@Slf4j
public class Steno {

    @Getter
    @Setter
    private boolean listen = false;

    @Getter
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    @Getter
    private Story story = new Story();

    @Getter
    private WebDriver driver;

    public Steno(WebDriver driver) {
        this.driver = driver;
    }

    public Scene createScene() {
        if (story == null) {
            return null;
        }

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

    @SneakyThrows
    private void saveStoryData() {
        String json = Steno.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(story);
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, story.getDataFilename());
        Files.write(json.getBytes(StandardCharsets.UTF_8), to);

        log.info("steno data location: {}", story.getStoryDir());
    }

    @SneakyThrows
    private void saveSceneData() {
        Scene scene = story.lastScene();

        String json = Steno.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(scene);
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, scene.getDataFilename());
        Files.write(json.getBytes(StandardCharsets.UTF_8), to);
    }

    @SneakyThrows
    private void saveScreenshot() {
        Scene scene = story.lastScene();

        File from = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, scene.getScreenshotFilename());
        Files.copy(from, to);
    }

    @SneakyThrows
    private void saveSource() {
        Scene scene = story.lastScene();

        String source = driver.getPageSource();
        File storyDir = story.getStoryDir();
        File to = new File(storyDir, scene.getPageSourceFilename());
        Files.write(source.getBytes(StandardCharsets.UTF_8), to);
    }

}
