package com.woozooha.steno.model;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Rectangle;

@Getter
@Setter
public class Element {

    private Rectangle rect;

    private String by;

    private String fieldName;

}
