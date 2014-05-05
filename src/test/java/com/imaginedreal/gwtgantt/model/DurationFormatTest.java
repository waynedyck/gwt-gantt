package com.imaginedreal.gwtgantt.model;

import org.junit.Test;

import com.imaginedreal.gwtgantt.model.DurationFormat;

import static org.junit.Assert.*;


public class DurationFormatTest {

	
    @Test
    public void formatTest() {
    	String zeroDays = DurationFormat.format(DurationFormat.DAYS, 0);
    	String oneDay = DurationFormat.format(DurationFormat.DAYS, 1);
    	String onePointOneDays = DurationFormat.format(DurationFormat.DAYS, 1.1);
    	String twoDays = DurationFormat.format(DurationFormat.DAYS, 2);
    	String oneHour = DurationFormat.format(DurationFormat.HOURS, 1);
    	String twoHours = DurationFormat.format(DurationFormat.HOURS, 2);
    	String oneMonth = DurationFormat.format(DurationFormat.MONTHS, 1);
    	String twoMonths = DurationFormat.format(DurationFormat.MONTHS, 2);
    	
    	assertTrue(zeroDays.equals("0 days"));
    	assertTrue(oneDay.equals("1 day"));
    	assertTrue(onePointOneDays.equals("1.1 days"));
    	assertTrue(twoDays.equals("2 days"));
    	assertTrue(oneHour.equals("1 hr"));
    	assertTrue(twoHours.equals("2 hrs"));
    	assertTrue(oneMonth.equals("1 mon"));
    	assertTrue(twoMonths.equals("2 mons"));
    	
    }
	
}
