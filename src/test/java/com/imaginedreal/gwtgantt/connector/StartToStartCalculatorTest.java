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

import com.imaginedreal.gwtgantt.connector.StartToStartCalculator;
import com.imaginedreal.gwtgantt.geometry.Point;
import com.imaginedreal.gwtgantt.geometry.Rectangle;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Tests for {@link StartToStartCalculator}
 * @author Brad Rydzewski
 */
public class StartToStartCalculatorTest {

    /**
     * Instance of {@link  StartToStartCalculator}.
     */
    private StartToStartCalculator calculator = new  StartToStartCalculator();

    /**
     * Tests calculating connector for 2 rectangles that both have the
     * same left coordinates. In terms of a gantt chart, this would
     * mean both tasks start at the same time.
     *
     * The connector should render coordinates that look like the
     * following plotted points:
     * <pre>
     *   x------o
     *   |
     *   x------>
     * </pre>
     */
    @Test
    public void testCalculate_SameLeftCoordinates() {


        Rectangle firstTask = new Rectangle(20,10,100,20); //left, top, width, height
        Rectangle secondTask = new Rectangle(20,30,100,20);

        Point[] points = calculator.calculate(firstTask, secondTask);

        assertTrue("Points are not null",points!=null);
        assertTrue("Four points make up connection",points.length==4);
        
        assertTrue("Point 1 is (20,20)",points[0].getX()==20 && points[0].getY()==20);
        assertTrue("Point 2 is (5,20) ",points[1].getX()== 5 && points[1].getY()==20);
        assertTrue("Point 3 is (5,40) ",points[2].getX()== 5 && points[2].getY()==40);
        assertTrue("Point 4 is (20,40)",points[3].getX()==20 && points[3].getY()==40);
    }
}