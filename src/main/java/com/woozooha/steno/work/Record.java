package com.woozooha.steno.work;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woozooha.steno.model.Story;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Record {

    @JsonIgnore
    private Class<? extends Surfer> surferClass;

    private String surfer;

    private LocalDateTime start;

    private LocalDateTime end;

    private Story story;

}
