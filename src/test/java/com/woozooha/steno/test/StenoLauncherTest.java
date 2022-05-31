package com.woozooha.steno.test;

import com.woozooha.steno.StenoRunTest;
import org.junit.jupiter.api.Test;

class StenoLauncherTest {

    @Test
    public void execute() {
        StenoLauncher launcher = new StenoLauncher();
        launcher.execute(StenoRunTest.class);
    }

}