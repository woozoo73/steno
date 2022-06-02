package com.woozooha.steno.example.wikipedia;

import com.woozooha.steno.example.common.ChromeFactory;
import com.woozooha.steno.test.StenoExtension;
import com.woozooha.steno.test.StenoTest;
import com.woozooha.steno.test.StenoWebDriverFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(StenoExtension.class)
@StenoTest
@StenoWebDriverFactory(ChromeFactory.class)
@Slf4j
class StenoTest7 extends StenoTest5 {
}
