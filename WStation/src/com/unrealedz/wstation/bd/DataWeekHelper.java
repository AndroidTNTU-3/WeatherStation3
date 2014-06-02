package com.unrealedz.wstation.bd;

import java.util.ArrayList;
import java.util.List;

import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.entity.ForecastDayShort;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/////////////////////////////////
//Helper: info of week forecast//
//for save to week forecast DB //	
/////////////////////////////////

public class DataWeekHelper {

	private SQLiteDatabase db;
	private List<ForecastDay> forecastDays;
	private DbHelper openHelper;
	Cursor cursorGetTemperatureDay;
	Cursor cursorGetCursor;
	Cursor cursorGetCursorDay;
	
	public static final String[] KEYS_TEMPERATURE_DAY = {DbHelper.DATE, 
												"MIN(" + DbHelper.TEMPERATURE_MIN + ") AS " +  DbHelper.TEMPERATURE_MIN,
												"MAX(" + DbHelper.TEMPERATURE_MAX + ") AS " +  DbHelper.TEMPERATURE_MAX};
	public static final String[] KEYS = {DbHelper.TEMPERATURE_MIN, DbHelper.TEMPERATURE_MAX};
	
	public DataWeekHelper(Context context) {
        /*openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();*/
		openHelper = DbHelper.getInstance(context);
        db = openHelper.getWritableDatabase();
       
    }
	
	public void insertDayItem(Forecast forecast) {
		forecastDays = forecast.getForecastDays();
		ForecastDay forecastDay = new ForecastDay();
		for(ForecastDay fd: forecastDays){
			forecastDay.setDate(fd.getDate());
			forecastDay.setHour(fd.getHour());
			forecastDay.setCloudId(fd.getCloudId());
			forecastDay.setPictureName(fd.getPictureName());
			forecastDay.setPpcp(fd.getPpcp());
			forecastDay.setTemperatureMin(fd.getTemperatureMin());
			forecastDay.setTemperatureMax(fd.getTemperatureMax());
			forecastDay.setPressureMin(fd.getPressureMin());
			forecastDay.setPressureMax(fd.getPressureMax());
			forecastDay.setWindMin(fd.getWindMin());
			forecastDay.setWindMax(fd.getWindMax());
			forecastDay.setWindRumb(fd.getWindRumb());
			forecastDay.setHumidityMin(fd.getHumidityMin());
			forecastDay.setHumidityMax(fd.getHumidityMax());
			forecastDay.setWpi(fd.getWpi());
			insertForecast(forecastDay);
		}

		
	}
	
	 private ContentValues getDayValues(ForecastDay forecastDay) {
	        ContentValues values = new ContentValues();
	        values.put(DbHelper.DATE, forecastDay.getDate());
	        values.put(DbHelper.HOUR, forecastDay.getHour());
	        values.put(DbHelper.CLOUD_ID, forecastDay.getCloudId());
	        values.put(DbHelper.PICTURE_NAME, forecastDay.getPictureName());
	        values.put(DbHelper.PPCP, forecastDay.getPpcp());
	        values.put(DbHelper.TEMPERATURE_MIN, forecastDay.getTemperatureMin());
	        values.put(DbHelper.TEMPERATURE_MAX, forecastDay.getTemperatureMax());
	        values.put(DbHelper.PRESSURE_MIN, forecastDay.getPressureMin());
	        values.put(DbHelper.PRESSURE_MAX, forecastDay.getPressureMax());
	        values.put(DbHelper.WIND_MIN, forecastDay.getWindMin());
	        values.put(DbHelper.WIND_MAX, forecastDay.getWindMax());
	        values.put(DbHelper.WIND_RUMB, forecastDay.getWindRumb());
	        values.put(DbHelper.HUMIDITY_MIN, forecastDay.getHumidityMin());
	        values.put(DbHelper.HUMIDITY_MAX, forecastDay.getHumidityMax());
	        values.put(DbHelper.WPI, forecastDay.getWpi());
	        return values;
	 }
	
	public Cursor getTemperatureDay(String tableName) {
		/*Cursor c = 	db.query(tableName, KEYS_TEMPERATURE_DAY, 
				null, null, DbHelper.DATE, null, null, null);*/
		/*String sql = "SELECT " +  DbHelper.DATE + ", " + DbHelper.TEMPERATURE_MIN + ", " + DbHelper.TEMPERATURE_MAX + ", "
				+ DbHelper.PICTURE_NAME +  " FROM (SELECT " + DbHelper.DATE + ", MIN(" + DbHelper.TEMPERATURE_MIN + ") AS " 
				+ DbHelper.TEMPERATURE_MIN + ", MAX(" + DbHelper.TEMPERATURE_MAX + ") AS " + DbHelper.TEMPERATURE_MAX + ", " +
				DbHelper.PICTURE_NAME + ", " + DbHelper.HOUR + " FROM " + tableName + " WHERE " + DbHelper.HOUR + " = 15 GROUP BY " + DbHelper.DATE + ")";
		Cursor c = db.rawQuery(sql, null);*/
		
		String sql = "SELECT T1._id AS _id, T1.date AS date, T1.temperatureMin AS temperatureMin, T1.temperatureMax AS temperatureMax, "
				+ "T2.pictureName AS pictureName, T2.cloudId AS cloudId FROM "
				+ "(SELECT _id, date, temperatureMin, temperatureMax FROM (SELECT _id, date, MIN(temperatureMin) AS temperatureMin, MAX(temperatureMax) "
				+ "AS temperatureMax FROM week GROUP BY date)) T1,  (SELECT date, pictureName, cloudId FROM week WHERE hour = 15) T2 WHERE T1.date = T2.date";
		
		cursorGetTemperatureDay = db.rawQuery(sql, null);
		if (cursorGetTemperatureDay != null) {
			cursorGetTemperatureDay.moveToFirst();
		}
		return cursorGetTemperatureDay;
	}
	
	//Get list of 5 day forecast in short format(date, picture name, tMin, tMax)
	
	public List<ForecastDayShort> getShortWeek() {

		List<ForecastDayShort> forecastDaysShort = null;
		String sql = "SELECT T1._id AS _id, T1.date AS date, T1.temperatureMin AS temperatureMin, T1.temperatureMax AS temperatureMax, "
				+ "T2.pictureName AS pictureName, T2.cloudId AS cloudId FROM "
				+ "(SELECT _id, date, temperatureMin, temperatureMax FROM (SELECT _id, date, MIN(temperatureMin) AS temperatureMin, MAX(temperatureMax) "
				+ "AS temperatureMax FROM week GROUP BY date)) T1,  (SELECT date, pictureName, cloudId FROM week WHERE hour = 15) T2 WHERE T1.date = T2.date";
		
		Cursor cursor = db.rawQuery(sql, null);
		
		if (cursor.getCount() != 0) {

			forecastDaysShort = new ArrayList<ForecastDayShort>();
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

		}
		
		if (cursor != null) cursor.close();
		return forecastDaysShort;
	}
	
	public List<ForecastDay> getForecastDayHours(String date){
		
		List<ForecastDay> forecastDays = null;
		
		String[] selectionArgs = new String[] { date };
		Cursor cursor = db.query(DbHelper.WEEK_TABLE, null, 
				DbHelper.DATE + "=?", selectionArgs, null, null, null, null);
		
		if (cursor.getCount() != 0) {
			
			cursor.moveToFirst();
			forecastDays = new ArrayList<ForecastDay>();
			
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
			
		}
		
		if (cursor != null) cursor.close();	
		return forecastDays;
		
	}
	
	public void cleanOldRecords() {
        db.delete(DbHelper.WEEK_TABLE, null, null);
    }
	
	private long insertForecast(ForecastDay forecastDay) {
	       Log.i("DEBUG DB", "insertWeek");
	        ContentValues values = getDayValues(forecastDay);
	        return db.insert(DbHelper.WEEK_TABLE, null, values);
	    }
	
	 public Cursor getCursor(String tableName) {
	    	cursorGetCursor = db.query(tableName, null, null, null, null, null, null);
	        return cursorGetCursor;
	    }
	


	public Cursor getCursorDay(String date) {
		String[] selectionArgs = new String[] { date };
		Cursor cursorGetCursorDay = 	db.query(DbHelper.WEEK_TABLE, null, 
				DbHelper.DATE + "=?", selectionArgs, null, null, null, null);
		if (cursorGetCursorDay != null) {
			cursorGetCursorDay.moveToFirst();
		}
		return cursorGetCursorDay;
		
	}
	
	public void closeCursorTemperatureDay(){
		if (cursorGetTemperatureDay != null) cursorGetTemperatureDay.close();
	}

	public void closeDB() {
		if (db != null && db.isOpen()) {
			db.close();
			db = null;
        }
		//openHelper.close();     
    }
	
}
