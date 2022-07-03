package com.woozooha.steno.writer;

import com.woozooha.steno.model.Story;
import com.woozooha.steno.model.StoryExample;

import java.util.List;

public interface Reader {

    List<Story> getStoryList(StoryExample example);

    Story getStory(String id);

}
