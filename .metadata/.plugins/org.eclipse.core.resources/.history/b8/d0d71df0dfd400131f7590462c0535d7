package com.unrealedz.wstation.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

public class Utils {
	
		
	public static String getStringDate(String date){
		
		Date mdate = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat newFormatter = new SimpleDateFormat("EEEE dd MMMM yyyy");
		try {
			mdate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		String dateWeek = newFormatter.format(mdate);
		return dateWeek;
	}
	
	public static int getImageId(String pictureName, Context context){
		
		pictureName = pictureName.substring(0, pictureName.lastIndexOf("."));
		int id_image = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		return id_image;
		
	}
	
public static int getBigImageId(String pictureName, Context context){
		
		pictureName = pictureName.substring(0, pictureName.lastIndexOf(".")) + "_big";
		int id_image = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		return id_image;
		
	}
		

}
