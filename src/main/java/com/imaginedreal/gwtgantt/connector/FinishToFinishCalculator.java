/*
 * This file is part of gwt-gantt
 * Copyright (C) 2010  Scottsdale Software LLC
 * 
 * gwt-gantt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package com.imaginedreal.gwtgantt.connector;

import com.imaginedreal.gwtgantt.geometry.Point;
import com.imaginedreal.gwtgantt.geometry.Rectangle;

/**
 * Calculate the path to connect the Right edges of two
 * rectangles with a polygonal line.
 * 
 * @author Brad Rydzewski
 *
 */
public class FinishToFinishCalculator implements Calculator {

    protected static final int markerSize = 8;
    protected static final int rightPadding = 15;
	
    @Override
    public Point[] calculate(Rectangle r1, Rectangle r2) {

        assert(r1!=null);
        assert(r2!=null);

        int right = Math.max(r1.getRight(), r2.getRight()) + rightPadding; //+ markerSize

        Point[] points = new Point[4];
        points[0] = new Point(r1.getRight(), r1.getTop() + r1.getHeight() / 2);
        points[1] = new Point(right, r1.getTop() + r1.getHeight() / 2);
        points[2] = new Point(right, r2.getTop() + r2.getHeight() / 2);
        points[3] = new Point(r2.getRight(), r2.getTop() + r2.getHeight() / 2); //+ markerSize

        return points;
    }

    @Override
    public Point[] calculateWithOffset(Rectangle r1, Rectangle r2) {
        Point[] points = calculate(r1,r2);
        points[3].setX(points[3].getX()+markerSize);
        return points;
    }
}
