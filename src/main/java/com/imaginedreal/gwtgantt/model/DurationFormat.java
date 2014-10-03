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
package com.imaginedreal.gwtgantt.model;

public enum DurationFormat {
	MINUTES,
	HOURS,
	DAYS,
	WEEKS,
	MONTHS,
	YEARS,
	PERCENT,
	NONE;

	public static String format(DurationFormat timeUnit, double value) {
		String format;
		switch (timeUnit) {
		case HOURS: format="hr"; break;
		case MINUTES: format="min"; break;
		case DAYS: format="day"; break;
		case WEEKS: format="wk"; break;
		case MONTHS: format="mon"; break;
		case YEARS: format="year"; break;
		case PERCENT: format="%"; break;
		case NONE:
		default: format=""; break;
		}
		
		if(timeUnit!=NONE) {
                        String valueString = (value%1==0)?Integer.toString((int)value):Double.toString(value);
			format = valueString + " " + format;
			format += (timeUnit==PERCENT || value==1)?"":"s";
		}
		
		return format;
	}
}