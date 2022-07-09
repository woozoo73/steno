package com.woozooha.steno.conf;

import com.woozooha.steno.history.FileHistory;
import com.woozooha.steno.history.History;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {

    private History history;

    @Getter
    @Setter
    private static Config current = defaultConfig();

    public static Config defaultConfig() {
        Config config = new Config();
        config.setHistory(new FileHistory());

        return config;
    }

}
