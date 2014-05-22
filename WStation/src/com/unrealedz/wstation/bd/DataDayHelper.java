package com.unrealedz.wstation.bd;

////////////////////////////////////
//Helper: info of current forecast//
////////////////////////////////////

import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.entity.Forecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataDayHelper {
	
private SQLiteDatabase db;

private Cursor cursor;
			
	public DataDayHelper(Context context) {
        DbHelper openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();
       
    }
	
	public long insertDayItem(Forecast forecast) {
		
		CurrentForecast currentForecast = new CurrentForecast();
		
		CurrentForecast cf = new CurrentForecast();
		cf = forecast.getCurrentForecast();
		
		currentForecast.setLastUpdated(cf.getLastUpdated());
		currentForecast.setExpires(cf.getExpires());
		currentForecast.setTime(cf.getTime());
		currentForecast.setCloudId(cf.getCloudId());
		currentForecast.setPictureName(cf.getPictureName());
		currentForecast.setTemperature(cf.getTemperature());
		currentForecast.setTemperatureFlik(cf.getTemperatureFlik());
		currentForecast.setPressure(cf.getPressure());
		currentForecast.setWind(cf.getWind());
		currentForecast.setWindGust(cf.getWindGust());
		currentForecast.setWindRumb(cf.getWindRumb());
		currentForecast.setHumidity(cf.getHumidity());
		
        return insertCurrentForecast(currentForecast);
    }
	
	 public Cursor getCursor(String tableName) {	    	
	    	cursor = db.query(tableName, null, null, null, null, null, null);
	        return cursor;
	 }
	 
	//Get current forecast info
		public CurrentForecast getCurrentForecast(){
			
			CurrentForecast currentForecast = null;
			Cursor cursor = db.query(DbHelper.CURRENT_DAY_TABLE, null, null, null, null, null, null);
			
			if(cursor.getCount() != 0){
				cursor.moveToFirst();
				currentForecast= new CurrentForecast();
				
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
			}
			
			if(cursor != null) cursor.close();
			return currentForecast;
			
		}
	 
		
	public void cleanOldRecords() {
        db.delete(DbHelper.CURRENT_DAY_TABLE, null, null);
    }
	
	private long insertCurrentForecast(CurrentForecast currentForecast) {
	       Log.i("DEBUG DB", "insertFeed");
	        ContentValues values = getCurrentDayValues(currentForecast);
	        return db.insert(DbHelper.CURRENT_DAY_TABLE, null, values);
	    }

	private ContentValues getCurrentDayValues(CurrentForecast currentForecast) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.LAST_UPDATED, currentForecast.getLastUpdated());
		values.put(DbHelper.EXPIRES, currentForecast.getExpires());
		values.put(DbHelper.TIME, currentForecast.getTime());
		values.put(DbHelper.CLOUD_ID, currentForecast.getCloudId());
		values.put(DbHelper.PICTURE_NAME, currentForecast.getPictureName()
				.toLowerCase());
		values.put(DbHelper.TEMPERATURE, currentForecast.getTemperature());
		values.put(DbHelper.TEMPERATURE_FLIK,
				currentForecast.getTemperatureFlik());
		values.put(DbHelper.PRESSURE, currentForecast.getPressure());
		values.put(DbHelper.WIND, currentForecast.getWind());
		values.put(DbHelper.WIND_GUST, currentForecast.getWindGust());
		values.put(DbHelper.WIND_RUMB, currentForecast.getWindRumb());
		values.put(DbHelper.HUMIDITY, currentForecast.getHumidity());
		return values;
	}

	public void closeDB() {
		db.close();
	}
}
