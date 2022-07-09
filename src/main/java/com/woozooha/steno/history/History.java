package com.woozooha.steno.history;

import com.woozooha.steno.model.Scene;
import com.woozooha.steno.model.Story;
import com.woozooha.steno.model.StoryExample;

import java.io.File;
import java.util.List;

public interface History {

    void write(Story story);

    void write(Story story, Scene scene);

    void write(Story story, Scene scene, File screenshot);

    void write(Story story, Scene scene, String source);

    List<Story> getStoryList(StoryExample example);

    Story getStory(String id);

}
