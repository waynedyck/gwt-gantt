package com.imaginedreal.gwtgantt.connector;

import com.imaginedreal.gwtgantt.geometry.Point;
import com.imaginedreal.gwtgantt.geometry.Rectangle;

public interface Calculator {
    /**
     * Calculates the path to connect two Rectangles with a line.
     * @param r1
     * @param r2
     * @return
     */
    public Point[] calculate(Rectangle r1, Rectangle r2);

    /**
     * Calculates the path to connect two Rectangles with a line,
     * including any offset required for arrows. In the case of SVG-drawn
     * paths, the arrow is added <b>after</b> the path ends. This means
     * we need to adjust the end point in the path to account for the
     * arrow's size.
     * @param r1
     * @param r2
     * @return
     */
    public Point[] calculateWithOffset(Rectangle r1, Rectangle r2);
}
