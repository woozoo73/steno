package com.woozooha.steno.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Scene {

    private int id;

    private Class<?> pageClass;

    private String url;

    private String title;

    private List<Element> elements = new ArrayList<>();

    private List<Event> events = new ArrayList<>();

    public Scene(int id) {
        this.id = id;
    }

    @JsonIgnore
    public String getDataFilename() {
        return String.format("scene-%d.json", id);
    }

    @JsonIgnore
    public String getScreenshotFilename() {
        return String.format("scene-%d.png", id);
    }

    @JsonIgnore
    public String getPageSourceFilename() {
        return String.format("scene-%d.html", id);
    }

}
