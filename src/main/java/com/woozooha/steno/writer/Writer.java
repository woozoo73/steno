package com.woozooha.steno.writer;

import com.woozooha.steno.model.Scene;
import com.woozooha.steno.model.Story;

import java.io.File;

public interface Writer {

    void write(Story story);

    void write(Story story, Scene scene);

    void write(Story story, Scene scene, File screenshot);

    void write(Story story, Scene scene, String source);

}
