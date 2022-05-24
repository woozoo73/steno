package com.woozooha.steno;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Scene {

    @JsonIgnore
    private Long id;

    private Class<?> pageClass;

    private String url;

    private String title;

    private List<Element> elements = new ArrayList<>();

    public Scene(Long id) {
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
