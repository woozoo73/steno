package com.woozooha.steno;

import lombok.Builder;
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
@Builder
public class Context {

    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String TIME_PATTERN = "HHmmss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    private String date;
    private String time;
    private String uuid;

    private String id;

    @Builder.Default
    private List<Page> pages = new ArrayList<>();

    public Context() {
        initId();
        initDirectory();
    }

    protected void initId() {
        LocalDateTime now = LocalDateTime.now();
        this.date = DATE_FORMATTER.format(now);
        this.time = TIME_FORMATTER.format(now);
        this.uuid = pseudoUuid();
        this.id = String.format("%s-%s-%s", date, time, uuid);
    }

    public void initDirectory() {
        File root = new File("/steno");
        File dateDir = new File(root, date);
//        File testDir = new File(dateDir)
    }

    protected String pseudoUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
