package com.woozooha.steno.history;

import com.woozooha.steno.model.Story;
import com.woozooha.steno.model.StoryExample;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class FileHistoryTest {

    @Test
    void getStoryList() {
        FileHistory history = new FileHistory(getStenoDataDir());

        StoryExample example = new StoryExample();
        example.setDate("20220703");

        List<Story> storyList = history.getStoryList(example);

        assertNotNull(storyList);
        assertThat(storyList.size(), is(1));
    }

    @Test
    void getStory() {
        FileHistory history = new FileHistory(getStenoDataDir());

        String id = "20220703-112018-4ec01d78";
        Story story = history.getStory(id);
        log.info("story={}", story);

        assertNotNull(story);
        assertThat(story.getId(), is(id));
    }

    String getStenoDataDir() {
        String path = "src/test/resources/steno-data";
        File file = new File(path);

        return file.getAbsolutePath();
    }

}
