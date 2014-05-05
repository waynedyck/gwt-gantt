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

import com.imaginedreal.gwtgantt.model.PredecessorType;

/**
 * Factory to get an instance of a {@link Calculator} based upon
 * a {@link ConnectionType}.
 * @author Brad Rydzewski
 */
public class CalculatorFactory {

    private static final Calculator START_TO_START = new StartToStartCalculator();
    private static final Calculator START_TO_FINISH = new StartToFinishCalculator();
    private static final Calculator FINISH_TO_START = new FinishToStartCalculator();
    private static final Calculator FINISH_TO_FINISH = new FinishToFinishCalculator();

    public static Calculator get(PredecessorType type) {

        Calculator calculator = null;

        switch (type) {
            case SS:
                calculator = START_TO_START;
                break;
            case SF:
                calculator = START_TO_FINISH;
                break;
            case FS:
                calculator = FINISH_TO_START;
                break;
            case FF:
                calculator = FINISH_TO_FINISH;
                break;
        }

        return calculator;
    }
}
