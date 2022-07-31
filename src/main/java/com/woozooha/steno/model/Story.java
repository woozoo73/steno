package com.woozooha.steno.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Story {

    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String TIME_PATTERN = "HHmmss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    private String id;

    private String groupId;

    private Class<?> targetClass;

    private Method targetMethod;

    @JsonIgnore
    private String date;
    @JsonIgnore
    private String time;
    @JsonIgnore
    private String uuid;

    private List<Page> pages = new ArrayList<>();

    private List<Scene> scenes = new ArrayList<>();

    public Story(String groupId, Class<?> targetClass, Method targetMethod) {
        this.groupId = groupId;
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;

        initId();
    }

    public Page lastPage() {
        if (pages.isEmpty()) {
            return null;
        }

        return pages.get(pages.size() - 1);
    }

    public Scene lastScene() {
        if (scenes.isEmpty()) {
            return null;
        }

        return scenes.get(scenes.size() - 1);
    }

    @JsonIgnore
    public String getDataFilename() {
        return String.format("%s.json", id);
    }

    protected void initId() {
        LocalDateTime now = LocalDateTime.now();
        this.date = DATE_FORMATTER.format(now);
        this.time = TIME_FORMATTER.format(now);
        this.uuid = pseudoUuid();
        String simpleClassName = targetClass == null ? "null" : targetClass.getSimpleName();
        String methodName = targetMethod == null ? "null" : targetMethod.getName();
        this.id = String.format("%s-%s-%s-%s-%s-%s", groupId, simpleClassName, methodName, date, time, uuid);
    }

    protected String pseudoUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
