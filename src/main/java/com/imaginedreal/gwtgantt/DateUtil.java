/*
 * This file is part of gwt-gantt
 * 
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

package com.imaginedreal.gwtgantt;

import java.util.Date;

import com.google.gwt.core.client.GWT;

/**
 *
 * @author Brad Rydzewski
 */
@SuppressWarnings("deprecation")
public class DateUtil {

	public static final int DAYS_PER_WEEK = 7;
	public static final int MONTHS_PER_QUARTER = 3;
	public static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	public static int getFullYear(Date date) {
		return 1900 + date.getYear();
	}

	public static Date addDays(Date date, int days) {
		Date d = (Date) date.clone();
		d.setDate(d.getDate() + days);
		return d;
	}
	
	public static Date addMonths(Date date, int months) {
		Date d = (Date) date.clone();
		d.setMonth(d.getMonth() + months);
		return d;
	}

	public static Date getFirstDayOfWeek(Date date) {

		Date d = (Date) date.clone();
		int day = d.getDay();

		d.setDate(d.getDate() - day);
		return d;
	}

	public static int getWeekOfYear(Date date) {

		Date d = (Date) date.clone();

		int year = d.getYear();
		int day = d.getDay();

		// this only applies if week starts with Monday instead of Sunday
		// if(day == 0) day = 7;

		d.setDate(d.getDate() + (4 - day));

		double ZBDoCY = Math.floor((d.getTime() - new Date(year, 0, 1)
				.getTime()) / 86400000);
		double week = 1 + Math.floor(ZBDoCY / 7);
		return (int) week;
	}

	/**
	 * Returns the number of days between the passed dates.
	 * 
	 * @param endDate The upper limit of the date range
	 * @param startDate The lower limit of the date range
	 * @return The number of days between <code>endDate</code> and
	 *         <code>starDate</code> (inclusive)
	 */
	public static int differenceInDays(Date endDate, Date startDate) {
		int difference = 0;
		if (!areOnTheSameDay(endDate, startDate)) {
			int endDateOffset = -(endDate.getTimezoneOffset() * 60 * 1000);
			long endDateInstant = endDate.getTime() + endDateOffset;
			int startDateOffset = -(startDate.getTimezoneOffset() * 60 * 1000);
			long startDateInstant = startDate.getTime() + startDateOffset;
			double differenceDouble = (double) Math.abs(endDateInstant - startDateInstant) / (double) MILLIS_IN_A_DAY;
			GWT.log("  differenceDouble: "+differenceDouble);
			differenceDouble = Math.max(1.0D, differenceDouble);
			difference = (int) Math.round(differenceDouble);//added math.ceil .. then changed to floor... shit, which one???
		}
		return difference;
	}

	/**
	 * Indicates whether two dates are on the same date by comparing their day,
	 * month and year values. Time values such as hours and minutes are not
	 * considered in this comparison.
	 * 
	 * @param dateOne The first date to test
	 * @param dateTwo The second date to test
	 * @return <code>true</code> if both dates have their <code>date</code>,
	 *         <code>month</code> and <code>year</code> properties with the
	 *         <em>exact</em> same values (whatever they are)
	 */
	public static boolean areOnTheSameDay(Date dateOne, Date dateTwo) {
		return dateOne.getDate() == dateTwo.getDate()
				&& dateOne.getMonth() == dateTwo.getMonth()
				&& dateOne.getYear() == dateTwo.getYear();
	}
	
	public static Date clone(Date date) {
		return (Date)date.clone();
	}
	
	public static Date reset(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
}
