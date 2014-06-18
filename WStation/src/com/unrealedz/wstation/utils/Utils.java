package com.unrealedz.wstation.utils;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.unrealedz.wstation.R;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.entity.PolySector;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

public class Utils {
	
	
	//get format data for listView(FragmentList)	
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
	
	//get short week format for listView(FragmentList)	
	public static String getStringDayWeekShort(String date){
		
		Date mdate = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat newFormatter = new SimpleDateFormat("EE");
		try {
			mdate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		String dateWeek = newFormatter.format(mdate);
		return dateWeek;
	}
	
	//get short last upDate time format for (FragmentInfo)	
	public static String getStringLastUpDate(String date){
		
		Date mdate = null;
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:SS Z", Locale.UK);
		SimpleDateFormat newFormatter = new SimpleDateFormat("HH:mm", Locale.UK);
		try {
			mdate = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
				
		String dateWeek = newFormatter.format(mdate);
		return dateWeek;
	}
	
	//get image id from resource	
	public static int getImageId(String pictureName, Context context){
		
		pictureName = pictureName.substring(0, pictureName.lastIndexOf("."));
		int id_image = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		return id_image;
		
	}
	
	//get big size image id from resource for some fragments (FragmentCurrent, FragmentDay)
	public static int getBigImageId(String pictureName, Context context){
		
		pictureName = pictureName.substring(0, pictureName.lastIndexOf(".")) + "_big";
		int id_image = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		return id_image;
		
	}
	
	public static int getBigestImageId(String pictureName, Context context){
		
		pictureName = pictureName.substring(0, pictureName.lastIndexOf(".")) + "_bigest";
		int id_image = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		return id_image;
		
	}
	
	public static int getNormalImageId(String pictureName, Context context){
		
		pictureName = pictureName.substring(0, pictureName.lastIndexOf("."));
		int id_image = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		return id_image;
		
	}
	
	public static int getWidgetImageId(String pictureName, Context context){
		
		pictureName = pictureName.substring(0, pictureName.lastIndexOf(".")) + "_w";
		int id_image = context.getResources().getIdentifier((context.getPackageName() + ":drawable/" + pictureName), null, null);
		return id_image;
		
	}
	
	//get orientation wind 
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
	
	//get orientation Cloud forecast 
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
	
	public static String getFahrenheit(String temp){				//return temperature to current fragment
		if (temp.length() > 2) temp = temp.substring(2);			//if temperature as "+10" concat string to "10" 
		return String.valueOf(Integer.valueOf(temp)*9/5 + 32);
		
	}
	
	public static int getFahrenheit(int temp){						//return temperature to day fragment
		return temp*9/5 + 32;		
	}

	public static int getwindUnitSpeed(int windSpeed, String windUnitSpeed) {	//return forecast parameters
		
		if(windUnitSpeed.equals("miles/h")) windSpeed = windSpeed/1609;
		else if (windUnitSpeed.equals("km/h")) windSpeed = windSpeed/1000;
		else if(windUnitSpeed.equals("ft/s")) windSpeed = (int) (windSpeed*3.28);
					
		return windSpeed;
	}

	public static List<Point> getTemperatureNodes(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getTemperatureMax()));
		}
		return nodes;
	}
	
	public static List<Point> getPressureNodes(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getPressureMax()));
		}
		return nodes;
	}
	
	public static List<Point> getHumidityNodes(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getHumidityMax()));
		}
		return nodes;
	}

	public static List<PolySector> getChartPoint(int height, int width,
			List<Point> nodes) {
		
		if (nodes.size() != 0){
		List<Point> points = new ArrayList<Point>();
		List<Point> sortedPoints = new ArrayList<Point>(nodes);
		List<PolySector> newPoints = new ArrayList<PolySector>();  //polygons list
			
		Collections.sort(sortedPoints, new ValueSort());
		
		int MaxValue = sortedPoints.get(sortedPoints.size() - 1).y;
		int heightAxis = height - height/5;
		double multiplier = 0;
		
		if (heightAxis > MaxValue) multiplier = heightAxis/MaxValue;					//multiplier
		else {
			int ko = MaxValue/heightAxis;
			MaxValue = MaxValue/(ko*2);
			multiplier = heightAxis/MaxValue;
		}
		
		width -= Contract.PADDING_LEFT_RIGHT;
		height -= Contract.PADDING_LEFT_RIGHT;
		int deltaHour = width/(nodes.size()-1);
		int i = 0;
		
		Log.i("DEBUG", "Multipler:" + String.valueOf(multiplier) + "  Max: " + String.valueOf(MaxValue));
		for (Point p: nodes){
			Point point = new Point();
			point.x = deltaHour*i;
			if (p.y > 700) p.y = (p.y - 730)*3;
			point.y = (int) (p.y*multiplier);
			Log.i("DEBUG", "In utils" + String.valueOf(point.y));
			
			points.add(point);
			i++;
		}
		
		Log.i("DEBUG", "pointssize" + points.size());
		
		//set polygons
			for (int a = 0; a < points.size() - 1 ; a++) {

				PolySector ps = new PolySector();
				for (int j = 0; j < 4; j++) {

					switch (j) {
						case 0:
							int newX = Contract.MARGIN_LEFT + points.get(a).x;
							int newY = Contract.MARGIN_LEFT + height;
							ps.addToArray(new Point(newX, newY));
						break;
						case 1:
							newX = Contract.MARGIN_LEFT + points.get(a).x;
							newY = height - points.get(a).y;
							ps.addToArray(new Point(newX, newY));
						break;
						case 2:
							newX = Contract.MARGIN_LEFT + points.get(a + 1).x;
							newY = height - points.get(a + 1).y;
							ps.addToArray(new Point(newX, newY));
						break;
						case 3:
							newX = Contract.MARGIN_LEFT + points.get(a + 1).x;
							newY = Contract.MARGIN_LEFT + height;
							ps.addToArray(new Point(newX, newY));
						break;
						default:
						break;
					}


				}
				newPoints.add(ps);
				
			}

			return newPoints;
		}
		return null;
	}





}
