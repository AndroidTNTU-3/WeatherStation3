package com.unrealedz.wstation.utils;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.entity.ForecastDayShort;
import com.unrealedz.wstation.entity.City.Country;
import com.unrealedz.wstation.entity.City.Region;

public class UtilsDB {
	
	//Get list of 5 day forecast in short format(date, picture name, tMin, tMax)
	public static List<ForecastDayShort> getForecastListMain(Cursor cursor){
		List<ForecastDayShort> forecastDaysShort = new ArrayList<ForecastDayShort>();
		cursor.moveToFirst();
		do {
			ForecastDayShort forecastDay = new ForecastDayShort();
			forecastDay.setId(cursor.getLong(cursor.getColumnIndex(DbHelper.ID)));
			forecastDay.setDate(cursor.getString(cursor.getColumnIndex(DbHelper.DATE)));
			forecastDay.setPictureName(cursor.getString(cursor.getColumnIndex(DbHelper.PICTURE_NAME)));
			forecastDay.setTemperatureMin(cursor.getInt(cursor.getColumnIndex(DbHelper.TEMPERATURE_MIN)));
			forecastDay.setTemperatureMax(cursor.getInt(cursor.getColumnIndex(DbHelper.TEMPERATURE_MAX)));
			forecastDaysShort.add(forecastDay);
		} while(cursor.moveToNext());	
		return forecastDaysShort;
	}
	
}
