package com.woozooha.steno;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woozooha.steno.model.Story;
import com.woozooha.steno.util.ContextUtils;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

public class Steno {

    private static final ThreadLocal<Steno> STENO = new ThreadLocal<>();

    private static final ThreadLocal<Boolean> LISTEN = new ThreadLocal<>();

    @Getter
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        LISTEN.set(Boolean.FALSE);
    }

    @Getter
    private Story story;

    @Getter
    private WebDriver driver;

    public Steno(WebDriver driver) {
        this.driver = driver;
        init();
    }

    public static Steno start(WebDriver driver) {
        Steno steno = new Steno(driver);
        STENO.set(steno);
        LISTEN.set(Boolean.TRUE);

        return steno;
    }

    public static void end() {
        ContextUtils.saveStory();

        LISTEN.set(Boolean.FALSE);
        STENO.remove();
    }

    public static Steno currentSteno() {
        return STENO.get();
    }

    public static Boolean listen() {
        return LISTEN.get();
    }

    public static void listen(Boolean status) {
        LISTEN.set(status);
    }

    protected void init() {
        initStory();
    }

    protected void initStory() {
        story = new Story();
    }

}
