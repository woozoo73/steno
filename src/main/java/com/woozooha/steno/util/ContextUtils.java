package com.woozooha.steno.util;

import com.woozooha.steno.replace.PageFactoryInterceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.PageFactory;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

@Slf4j
public abstract class ContextUtils {

    public static void replacePageFactory() {
        try {
            ByteBuddyAgent.install();
            ByteBuddy byteBuddy = new ByteBuddy();
            byteBuddy
                    .redefine(PageFactory.class)
                    .method(named("initElements").and(takesArguments(SearchContext.class, Object.class)))
                    .intercept(MethodDelegation.to(PageFactoryInterceptor.class))
                    .make()
                    .load(
                            PageFactory.class.getClassLoader(),
                            ClassReloadingStrategy.fromInstalledAgent());
        } catch (Throwable t) {
            // ignore.
            log.error("Exception occurred.", t);
        }
    }

    @SneakyThrows
    public static void resetPageFactory() {
        try {
            ClassReloadingStrategy.fromInstalledAgent().reset(PageFactory.class);
        } catch (Throwable t) {
            // ignore.
            log.error("Exception occurred.", t);
        }
    }

}
