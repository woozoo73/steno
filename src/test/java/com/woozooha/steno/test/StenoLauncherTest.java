package com.woozooha.steno.test;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class StenoLauncherTest {

    @Test
    @SneakyThrows
    public void execute() {
        StenoLauncher launcher = new StenoLauncher();
        Class<?> clazz =  Class.forName("com.woozooha.steno.example.wikipedia.StenoTest1");
        launcher.execute(clazz);
    }

}
