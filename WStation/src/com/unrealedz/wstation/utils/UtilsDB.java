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
	
	//Get list of day forecast 	
	public static List<ForecastDay> getForecastList(Cursor cursor){
		List<ForecastDay> forecastDays = new ArrayList<ForecastDay>();
		do {
			ForecastDay forecastDay = new ForecastDay();
			forecastDay.setDate(cursor.getString(cursor.getColumnIndex(DbHelper.DATE)));
			forecastDay.setHour(cursor.getInt(cursor.getColumnIndex(DbHelper.HOUR)));
			forecastDay.setCloudId(cursor.getInt(cursor.getColumnIndex(DbHelper.CLOUD_ID)));
			forecastDay.setPictureName(cursor.getString(cursor.getColumnIndex(DbHelper.PICTURE_NAME)));
			forecastDay.setPpcp(cursor.getInt(cursor.getColumnIndex(DbHelper.PPCP)));
			forecastDay.setTemperatureMin(cursor.getInt(cursor.getColumnIndex(DbHelper.TEMPERATURE_MIN)));
			forecastDay.setTemperatureMax(cursor.getInt(cursor.getColumnIndex(DbHelper.TEMPERATURE_MAX)));
			forecastDay.setPressureMax(cursor.getInt(cursor.getColumnIndex(DbHelper.PRESSURE_MAX)));
			forecastDay.setPressureMin(cursor.getInt(cursor.getColumnIndex(DbHelper.PRESSURE_MIN)));
			forecastDay.setWindMin(cursor.getInt(cursor.getColumnIndex(DbHelper.WIND_MIN)));
			forecastDay.setWindMax(cursor.getInt(cursor.getColumnIndex(DbHelper.WIND_MAX)));
			forecastDay.setWindRumb(cursor.getInt(cursor.getColumnIndex(DbHelper.WIND_RUMB)));
			forecastDay.setHumidityMin(cursor.getInt(cursor.getColumnIndex(DbHelper.HUMIDITY_MIN)));
			forecastDay.setHumidityMax(cursor.getInt(cursor.getColumnIndex(DbHelper.HUMIDITY_MAX)));
			forecastDay.setWpi(cursor.getInt(cursor.getColumnIndex(DbHelper.WPI)));
			forecastDays.add(forecastDay);
		} while(cursor.moveToNext());	
		return forecastDays;
	}
	
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
