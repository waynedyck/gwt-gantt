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

import com.imaginedreal.gwtgantt.connector.FinishToFinishCalculator;
import com.imaginedreal.gwtgantt.geometry.Point;
import com.imaginedreal.gwtgantt.geometry.Rectangle;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Tests for {@link FinishToFinishCalculator}
 * @author Brad Rydzewski
 */
public class FinishToFinishCalculatorTest {

    /**
     * Instance of {@link FinishToFinishCalculator}.
     */
    private FinishToFinishCalculator calculator =
            new FinishToFinishCalculator();

    /**
     * Tests calculating connector for 2 rectangles that both have the
     * same right coordinates. In terms of a gantt chart, this would
     * mean both tasks end at the same time.
     *
     * The connector should render coordinates that look like the
     * following plotted points:
     * <pre>
     *   o------x
     *          |
     *   <------x
     * </pre>
     */
    @Test
    public void testCalculate_SameRightCoordinates() {

        Rectangle firstTask = new Rectangle(10,10,100,20); //left, top, width, height
        Rectangle secondTask = new Rectangle(10,30,100,20);
        
        Point[] points = calculator.calculate(firstTask, secondTask);

        assertTrue("Points are not null",points!=null);
        assertTrue("Four points make up connection",points.length==4);

        assertTrue("Point 1 is (110,20)",points[0].getX()==110 && points[0].getY()==20);
        assertTrue("Point 2 is (125,20)",points[1].getX()==125 && points[1].getY()==20);
        assertTrue("Point 3 is (125,40)",points[2].getX()==125 && points[2].getY()==40);
        assertTrue("Point 4 is (110,40)",points[3].getX()==110 && points[3].getY()==40);
    }

    /**
     * Tests calculating connector for 2 rectangles. In this case the
     * 1st Rectangles Right coordinate is Less Than the 2nd Rectangles
     * Right coordinate. In terms of a gantt chart, this would
     * mean the 1st task ends before the 2nd task.
     *
     * The connector should render coordinates that look like the
     * following plotted points:
     * <pre>
     *   o------x
     *          |
     *      <---x
     * </pre>
     */
    @Test
    public void testCalculate_OffsetRightCoordinates_LessThan() {

        Rectangle firstTask = new Rectangle(10,10,100,20); //left, top, width, height
        Rectangle secondTask = new Rectangle(30,30,100,20);

        Point[] points = calculator.calculate(firstTask, secondTask);

        assertTrue("Points are not null",points!=null);
        assertTrue("Four points make up connection",points.length==4);

        assertTrue("Point 1 is (110,20)",points[0].getX()==110 && points[0].getY()==20);
        assertTrue("Point 2 is (145,20)",points[1].getX()==145 && points[1].getY()==20);
        assertTrue("Point 3 is (145,40)",points[2].getX()==145 && points[2].getY()==40);
        assertTrue("Point 4 is (130,40)",points[3].getX()==130 && points[3].getY()==40);
    }

    /**
     * Tests calculating connector for 2 rectangles. In this case the
     * 1st Rectangles Right coordinate is Greater Than the 2nd Rectangles
     * Right coordinate. In terms of a gantt chart, this would
     * mean the 1st task ends after the 2nd task.
     *
     * The connector should render coordinates that look like the
     * following plotted points:
     * <pre>
     *      o---x
     *          |
     *   <------x
     * </pre>
     */
    @Test
    public void testCalculate_OffsetRightCoordinates_GreaterThan() {

        Rectangle firstTask = new Rectangle(30,10,100,20); //left, top, width, height
        Rectangle secondTask = new Rectangle(10,30,100,20);

        Point[] points = calculator.calculate(firstTask, secondTask);

        assertTrue("Points are not null",points!=null);
        assertTrue("Four points make up connection",points.length==4);

        assertTrue("Point 1 is (130,20)",points[0].getX()==130 && points[0].getY()==20);
        assertTrue("Point 2 is (145,20)",points[1].getX()==145 && points[1].getY()==20);
        assertTrue("Point 3 is (145,40)",points[2].getX()==145 && points[2].getY()==40);
        assertTrue("Point 4 is (110,40)",points[3].getX()==110 && points[3].getY()==40);
    }

    /**
     * Tests calculating connector for 2 rectangles. In this
     * case, the 2nd rectangle is plotted <u>above</u> the first rectangle.
     * This is similar to the {@code testCalculate_SameRightCoordinates} test,
     * however, the rectangles are reversed.
     *
     * The connector should render coordinates that look like the
     * following plotted points:
     * <pre>
     *   <------x
     *          |
     *   o------x
     * </pre>
     */
    @Test
    public void testCalculate_FirstRectangleBelowSecondRectangle() {

        Rectangle firstTask = new Rectangle(10,30,100,20); //left, top, width, height
        Rectangle secondTask = new Rectangle(10,10,100,20);

        Point[] points = calculator.calculate(firstTask, secondTask);

        assertTrue("Points are not null",points!=null);
        assertTrue("Four points make up connection",points.length==4);

        assertTrue("Point 1 is (110,40)",points[0].getX()==110 && points[0].getY()==40);
        assertTrue("Point 2 is (125,40)",points[1].getX()==125 && points[1].getY()==40);
        assertTrue("Point 3 is (125,20)",points[2].getX()==125 && points[2].getY()==20);
        assertTrue("Point 4 is (110,20)",points[3].getX()==110 && points[3].getY()==20);
    }
}