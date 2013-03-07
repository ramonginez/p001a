package com.nahmens.inventario.utils;

import java.util.Calendar;

public class Utils {

	private static int _value;

	private synchronized static int getAndIncrement() {
		if( _value == Integer.MAX_VALUE-1 ) {
			_value = 0;
		} else {
			_value ++;
		}
		return _value;
	}
	public static synchronized String generateId(){

		//		return NiM.getInstance().generateUniqueId();

		// create a java calendar instance
		Calendar calendar = Calendar.getInstance();

		// get a java.util.Date from the calendar instance.
		// this date will represent the current instant, or "now".
		java.util.Date now = calendar.getTime();

		// a java current time (now) instance
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

		String id = String.valueOf(currentTimestamp.getTime())+"-"+getAndIncrement();

		return id;

	}
}
