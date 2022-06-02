package com.woozooha.steno.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Page {

    private Class<?> pageClass;

    private String url;

    private String title;

    private List<Element> elements = new ArrayList<>();

    public Page() {
    }

}
