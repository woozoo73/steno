package com.woozooha.steno.conf;

import com.woozooha.steno.writer.FileHistory;
import com.woozooha.steno.writer.Reader;
import com.woozooha.steno.writer.Writer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Config {

    private Writer writer = new FileHistory();

    private Reader reader = new FileHistory();

}
