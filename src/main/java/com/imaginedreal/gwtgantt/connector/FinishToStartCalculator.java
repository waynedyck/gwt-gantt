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
public class FinishToStartCalculator implements Calculator {

    @Override
	public Point[] calculate(Rectangle r1, Rectangle r2) {

            Point[] points = null;
            int top = r1.getTop() + r1.getHeight()/2;

            if(r1.getRight() <= r2.getLeft()) {

                    points = new Point[3];
                    points[0] = new Point(r1.getRight(), top);
                    points[1] = new Point(r2.getLeft()+5, top);
                    points[2] = new Point(r2.getLeft()+5, r2.getTop()); //-10

            } else {

                    /*
                     *         0--
                     *           |
                     *  ----------
                     *  |
                     *  ---->
                     */
                    points = new Point[6];
                    points[0] = new Point(r1.getRight(), top);
                    points[1] = new Point(r1.getRight()+15, top);
                    points[2] = new Point(r1.getRight()+15, r1.getBottom()+7);
                    points[3] = new Point(r2.getLeft()-15, r1.getBottom()+7);
                    points[4] = new Point(r2.getLeft()-15, r2.getTop() + r2.getHeight() / 2);
                    points[5] = new Point(r2.getLeft(), r2.getTop() + r2.getHeight() / 2);

            }
		

            return points;
	}


    @Override
    public Point[] calculateWithOffset(Rectangle r1, Rectangle r2) {
        Point[] points = calculate(r1,r2);
//        points[0].setX(points[0].getX()-2);//offset for borders????
        if(points.length==3) {
            points[2].setY(points[2].getY()-8);
        } else {
            points[5].setX(points[5].getX()-8);
        }

        return points;
    }
}
