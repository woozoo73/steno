package com.woozooha.steno;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;

@Getter
@Setter
public class Element {

    private Rectangle rect;

    private By by;

}
