package com.woozooha.steno;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Story {

    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String TIME_PATTERN = "HHmmss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    private String id;

    private String date;
    private String time;
    private String uuid;

    private Long sceneId = 0L;

    @JsonIgnore
    private File dateDir;
    @JsonIgnore
    private File storyDir;

    private List<Scene> scenes = new ArrayList<>();

    public Story() {
        initId();
        initDirectory();
    }

    public Scene createScene() {
        Scene scene = new Scene(++sceneId);
        scenes.add(scene);

        return scene;
    }

    public Scene lastScene() {
        if (scenes.isEmpty()) {
            return null;
        }

        return scenes.get(scenes.size() - 1);
    }

    public String getDataFilename() {
        return String.format("%s.json", id);
    }

    protected void initId() {
        LocalDateTime now = LocalDateTime.now();
        this.date = DATE_FORMATTER.format(now);
        this.time = TIME_FORMATTER.format(now);
        this.uuid = pseudoUuid();
        this.id = String.format("%s-%s-%s", date, time, uuid);
    }

    protected void initDirectory() {
        // TODO: Get from config.
        File root = new File("/steno-data");
        dateDir = new File(root, date);
        storyDir = new File(dateDir, id);
        if (!storyDir.exists()) {
            storyDir.mkdirs();
        }
    }

    protected String pseudoUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
