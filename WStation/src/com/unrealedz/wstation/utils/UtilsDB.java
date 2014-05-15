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
	
	
	//Get city info
	public static City getCity(Cursor cursor){
		City city = new City();
		Country country = new Country();
		Region region = new Region();
		cursor.moveToFirst();

		city.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.ID)));
		city.setName(cursor.getString(cursor.getColumnIndex(DbHelper.CITY_NAME)));	
		city.setNameEn(cursor.getString(cursor.getColumnIndex(DbHelper.CITY_NAME_EN)));
		country.setCountryId(cursor.getInt(cursor.getColumnIndex(DbHelper.COUNTRY_ID)));
		country.setCountry(cursor.getString(cursor.getColumnIndex(DbHelper.COUNTRY)));
		country.setCountryEn(cursor.getString(cursor.getColumnIndex(DbHelper.COUNTRY_EN)));
		city.setCountry(country);
		region.setRegion(cursor.getString(cursor.getColumnIndex(DbHelper.REGION)));
		region.setRegionEn(cursor.getString(cursor.getColumnIndex(DbHelper.REGION_EN)));
		city.setRegion(region);
		return city;
	}
	
	//Get current forecast info
	public static CurrentForecast getCurrentForecast(Cursor cursor){
		CurrentForecast currentForecast= new CurrentForecast();
		
		cursor.moveToFirst();
		
		currentForecast.setLastUpdated(cursor.getString(cursor.getColumnIndex(DbHelper.LAST_UPDATED)));
		currentForecast.setExpires(cursor.getString(cursor.getColumnIndex(DbHelper.EXPIRES)));
		currentForecast.setTime(cursor.getString(cursor.getColumnIndex(DbHelper.TIME)));
		currentForecast.setCloudId(cursor.getInt(cursor.getColumnIndex(DbHelper.CLOUD_ID)));
		currentForecast.setPictureName(cursor.getString(cursor.getColumnIndex(DbHelper.PICTURE_NAME)));
		currentForecast.setTemperature(cursor.getString(cursor.getColumnIndex(DbHelper.TEMPERATURE)));
		currentForecast.setTemperatureFlik(cursor.getString(cursor.getColumnIndex(DbHelper.TEMPERATURE_FLIK)));
		currentForecast.setPressure(cursor.getInt(cursor.getColumnIndex(DbHelper.PRESSURE)));
		currentForecast.setWind(cursor.getInt(cursor.getColumnIndex(DbHelper.WIND)));
		currentForecast.setWindGust(cursor.getInt(cursor.getColumnIndex(DbHelper.WIND_GUST)));
		currentForecast.setWindRumb(cursor.getInt(cursor.getColumnIndex(DbHelper.WIND_RUMB)));
		currentForecast.setHumidity(cursor.getInt(cursor.getColumnIndex(DbHelper.HUMIDITY)));
		return currentForecast;
		
	}
	
}
