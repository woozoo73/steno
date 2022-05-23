package com.woozooha.steno;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

public class Steno {

    private String uuid;

    public Steno() {
        init();
    }

    public void init() {
        initContext();
        initInterceptor();
    }

    public void initContext() {

    }

    public void initInterceptor() {
        ByteBuddyAgent.install();
        new ByteBuddy()
                .redefine(PageFactory.class)
                .method(named("initElements").and(takesArguments(ElementLocatorFactory.class, Object.class)))
                .intercept(MethodDelegation.to(PageFactoryInterceptor.class))
                .make()
                .load(
                        PageFactory.class.getClassLoader(),
                        ClassReloadingStrategy.fromInstalledAgent());
    }

}
