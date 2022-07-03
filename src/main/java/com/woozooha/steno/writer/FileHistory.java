package com.woozooha.steno.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.woozooha.steno.model.Scene;
import com.woozooha.steno.model.Story;
import com.woozooha.steno.model.StoryExample;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FileHistory implements Writer, Reader {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String stenoDataDir;

    private File root;

    public FileHistory() {
        this("/steno-data");
    }

    public FileHistory(String stenoDataDir) {
        this.stenoDataDir = stenoDataDir;
        initRootDirectory();
    }

    @Override
    public void write(Story story) {
        try {
            initDateDirectory(story);

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(story);
            File storyDir = story.getStoryDir();
            File to = new File(storyDir, story.getDataFilename());
            Files.write(json.getBytes(StandardCharsets.UTF_8), to);

            log.info("steno data location: {}", story.getStoryDir());
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public void write(Story story, Scene scene) {
        try {
            initDateDirectory(story);

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scene);
            File storyDir = story.getStoryDir();
            File to = new File(storyDir, scene.getDataFilename());
            Files.write(json.getBytes(StandardCharsets.UTF_8), to);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public void write(Story story, Scene scene, File screenshot) {
        try {
            initDateDirectory(story);

            File storyDir = story.getStoryDir();
            File to = new File(storyDir, scene.getScreenshotFilename());
            Files.copy(screenshot, to);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public void write(Story story, Scene scene, String source) {
        try {
            initDateDirectory(story);

            File storyDir = story.getStoryDir();
            File to = new File(storyDir, scene.getPageSourceFilename());
            Files.write(source.getBytes(StandardCharsets.UTF_8), to);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    protected void initRootDirectory() {
        try {
            this.root = new File(stenoDataDir);
            if (!root.exists()) {
                root.mkdirs();
            }
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    protected File getStoryDir(Story story) {
        return getStoryDir(story.getDate(), story.getId());
    }

    protected File getDateDir(String date) {
        try {
            File dateDir = new File(root, date);

            return dateDir;
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }

        return null;
    }

    protected File getStoryDir(String date, String id) {
        try {
            File dateDir = new File(root, date);
            File storyDir = new File(dateDir, id);

            return storyDir;
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }

        return null;
    }

    protected void initDateDirectory(Story story) {
        try {
            File storyDir = getStoryDir(story);
            if (!storyDir.exists()) {
                storyDir.mkdirs();
            }
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public List<Story> getStoryList(StoryExample example) {
        File dateDir = getDateDir(example.getDate());
        if (!dateDir.exists()) {
            return Collections.emptyList();
        }

        File[] files = dateDir.listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        return Arrays.stream(files)
                .map(this::readStory)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Story getStory(String id) {
        String[] ids = id.split("-");
        if (ids.length != 3) {
            return null;
        }

        File storyDir = getStoryDir(ids[0], id);
        if (!storyDir.exists()) {
            return null;
        }

        return readStory(storyDir);
    }

    protected Story readStory(File storyDir) {
        Story story = new Story();
        story.setId(storyDir.getName());
        String dataFilename = story.getDataFilename();

        File file = new File(storyDir, dataFilename);
        try (java.io.Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return objectMapper.readValue(reader, Story.class);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }

        return null;
    }

}
