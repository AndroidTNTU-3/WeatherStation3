package com.unrealedz.wstation.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.unrealedz.wstation.R;

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
	
	public static String getWindOrient(int windRumb, Context context){
		String orient = "";
		if((windRumb <= 21) & (windRumb >=337)) orient = context.getString(R.string.north);
		else if((windRumb >= 22) & (windRumb <= 66)) orient = context.getString(R.string.northeast);
		else if((windRumb >= 67) & (windRumb <= 111)) orient = context.getString(R.string.east);
		else if((windRumb >= 112) & (windRumb <= 156)) orient = context.getString(R.string.southeast);
		else if((windRumb >= 157) & (windRumb <= 201)) orient = context.getString(R.string.sout);
		else if((windRumb >= 202) & (windRumb <= 246)) orient = context.getString(R.string.southwest);
		else if((windRumb >= 247) & (windRumb <= 291)) orient = context.getString(R.string.west);
		else if((windRumb >= 292) & (windRumb <= 336)) orient = context.getString(R.string.northwest);
		return orient;		
	}
	
	public static String getCloud(int cloudID, Context context){
		String cloud = "";
		if ((cloudID >= 0) & (cloudID <= 9)) cloud = context.getString(R.string.sunny);
		else if ((cloudID >= 10) & (cloudID <= 19)) cloud = context.getString(R.string.partly_cloudy);
		else if ((cloudID >= 20) & (cloudID <= 29)) cloud = context.getString(R.string.cloudy);
		else if ((cloudID >= 30) & (cloudID <= 39)) cloud = context.getString(R.string.havy_cloudy);
		else if ((cloudID >= 40) & (cloudID <= 49)) cloud = context.getString(R.string.short_rain);
		else if ((cloudID >= 50) & (cloudID <= 59)) cloud = context.getString(R.string.rain);
		else if ((cloudID >= 60) & (cloudID <= 69)) cloud = context.getString(R.string.storm_rain);
		else if ((cloudID >= 70) & (cloudID <= 79)) cloud = context.getString(R.string.hail);
		else if ((cloudID >= 80) & (cloudID <= 89)) cloud = context.getString(R.string.rainy_snow);
		else if ((cloudID >= 90) & (cloudID <= 99)) cloud = context.getString(R.string.snow);
		else if ((cloudID >= 100) & (cloudID <= 109)) cloud = context.getString(R.string.snowstorm);
		return cloud;		
	}
		

}
