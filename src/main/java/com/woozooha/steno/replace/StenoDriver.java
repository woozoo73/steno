package com.woozooha.steno.replace;

import com.woozooha.steno.Steno;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.openqa.selenium.WebDriver;

public class StenoDriver implements WebDriver {


    @Getter
    private final Steno steno;

    @Delegate
    private final WebDriver webDriver;

    public StenoDriver(Steno steno, WebDriver webDriver) {
        this.steno = steno;
        this.webDriver = webDriver;
    }

}
