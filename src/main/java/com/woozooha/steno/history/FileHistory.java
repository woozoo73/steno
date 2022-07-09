package com.woozooha.steno.history;

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
public class FileHistory implements History {

    @Getter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String DEFAULT_STENO_DIR = "/steno-data";

    private final String stenoDataDir;

    public FileHistory() {
        this(DEFAULT_STENO_DIR);
    }

    public FileHistory(String stenoDataDir) {
        this.stenoDataDir = stenoDataDir;
    }

    @Override
    public void write(Story story) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(story);
            File storyDir = getDirectory(story, true);
            File to = new File(storyDir, story.getDataFilename());
            Files.write(json.getBytes(StandardCharsets.UTF_8), to);

            log.info("steno data location: {}", getDirectory(story));
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public void write(Story story, Scene scene) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scene);
            File storyDir = getDirectory(story, true);
            File to = new File(storyDir, scene.getDataFilename());
            Files.write(json.getBytes(StandardCharsets.UTF_8), to);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public void write(Story story, Scene scene, File screenshot) {
        try {
            File storyDir = getDirectory(story, true);
            File to = new File(storyDir, scene.getScreenshotFilename());
            Files.copy(screenshot, to);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public void write(Story story, Scene scene, String source) {
        try {
            File storyDir = getDirectory(story, true);
            File to = new File(storyDir, scene.getPageSourceFilename());
            Files.write(source.getBytes(StandardCharsets.UTF_8), to);
        } catch (Throwable t) {
            log.warn(t.getMessage(), t);
        }
    }

    @Override
    public List<Story> getStoryList(StoryExample example) {
        File dateDir = getDateDirectory(example.getDate());
        if (!dateDir.exists()) {
            return Collections.emptyList();
        }

        File[] files = dateDir.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
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

    protected File getRootDirectory(boolean create) {
        File root = new File(stenoDataDir);
        if (!root.exists() && create) {
            root.mkdirs();
        }

        return root;
    }

    protected File getDateDirectory(String date) {
        return new File(stenoDataDir, date);
    }

    protected File getDirectory(Story story) {
        return getDirectory(story, false);
    }

    protected File getDirectory(Story story, boolean create) {
        File root = getRootDirectory(create);
        File dateDir = new File(root, story.getDate());
        File storyDir = new File(dateDir, story.getId());
        if (!storyDir.exists() && create) {
            storyDir.mkdirs();
        }

        return storyDir;
    }

    protected File getStoryDir(String date, String id) {
        File dateDir = new File(stenoDataDir, date);

        return new File(dateDir, id);
    }

}
