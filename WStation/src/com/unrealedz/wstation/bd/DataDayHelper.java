package com.unrealedz.wstation.bd;

import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.entity.Forecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataDayHelper {
	
private SQLiteDatabase db;
			
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
	    	Cursor cursor = db.query(tableName, null, null, null, null, null, null);
	        return cursor;
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
	        values.put(DbHelper.PICTURE_NAME, currentForecast.getPictureName().toLowerCase());
	        values.put(DbHelper.TEMPERATURE, currentForecast.getTemperature());
	        values.put(DbHelper.TEMPERATURE_FLIK, currentForecast.getTemperatureFlik());
	        values.put(DbHelper.PRESSURE, currentForecast.getPressure());
	        values.put(DbHelper.WIND, currentForecast.getWind());
	        values.put(DbHelper.WIND_GUST, currentForecast.getWindGust());
	        values.put(DbHelper.WIND_RUMB, currentForecast.getWindRumb());
	        values.put(DbHelper.HUMIDITY, currentForecast.getHumidity());
	        return values;
	    }
}
