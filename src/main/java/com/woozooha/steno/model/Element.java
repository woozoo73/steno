package com.woozooha.steno.model;

import lombok.*;
import org.openqa.selenium.Rectangle;

@Getter
@Setter
@EqualsAndHashCode(exclude = "rect")
public class Element {

    private Rect rect;

    private String by;

    private String fieldName;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rect {

        private int x0;

        private int y0;

        private int x1;

        private int y1;

        public static Rect of(Rectangle rectangle) {
            if (rectangle == null) {
                return null;
            }

            int x0 = rectangle.getX();
            int y0 = rectangle.getY();
            int x1 = x0 + rectangle.getWidth();
            int y1 = y0 + rectangle.getHeight();

            return new Rect(x0, y0, x1, y1);
        }

    }

}
