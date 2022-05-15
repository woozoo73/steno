package com.woozooha.steno;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

@Setter
@Getter
public class Page {

    private Long id;

    private String url;

    private String title;

    private String pageSource;

    private String script;

    private Set<Element> elements = new TreeSet<>();

}
