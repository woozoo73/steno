package com.woozooha.steno;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

@Getter
@Setter
public class Element {

    private WebElement webElement;

    private Rectangle rectangle;

}
