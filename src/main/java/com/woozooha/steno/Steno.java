package com.woozooha.steno;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woozooha.steno.model.Story;
import com.woozooha.steno.replace.PageFactoryInterceptor;
import com.woozooha.steno.util.ContextUtils;
import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

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

    public static void end(WebDriver driver) {
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
        initInterceptor();
        initStory();
    }

    protected void initInterceptor() {
        ByteBuddyAgent.install();
        new ByteBuddy()
                .redefine(PageFactory.class)
                .method(named("initElements").and(takesArguments(SearchContext.class, Object.class)))
                .intercept(MethodDelegation.to(PageFactoryInterceptor.class))
                .make()
                .load(
                        PageFactory.class.getClassLoader(),
                        ClassReloadingStrategy.fromInstalledAgent());
    }

    protected void initStory() {
        story = new Story();
    }

}
