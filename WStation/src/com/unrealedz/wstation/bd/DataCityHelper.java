package com.unrealedz.wstation.bd;

import java.util.List;

import com.unrealedz.wstation.entity.CitiesDB;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CityDB;
import com.unrealedz.wstation.entity.ForecastDay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataCityHelper {
	
	private SQLiteDatabase db;
	private List<CityDB> cities;
	
	public DataCityHelper(Context context) {
        DbHelper openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();
       
    }
	
	public static final String[] ALL_KEYS = new String[] {DbHelper.CITY_DB_ID, 
		DbHelper.CITY_DB_NAME, 
		DbHelper.CITY_DB_NAME_EN, 
		DbHelper.CITY_DB_REGION, 
		DbHelper.CITY_DB_COUNTRY, 
		DbHelper.CITY_DB_COUNTRY_ID};
	
	public void cleanOldFeeds() {
        db.delete(DbHelper.CITY_DB_TABLE, null, null);
    }
	
	public void insertCitiesDB(CitiesDB citiesDB){
		cities = citiesDB.getCityDB();
			CityDB cityDB = new CityDB();
			for(CityDB cdb: cities){
				cityDB.setCityID(cdb.getCityID());
				cityDB.setName(cdb.getName());
				cityDB.setNameEn(cdb.getNameEn());
				cityDB.setCountry(cdb.getCountry());
				cityDB.setCountryID(cdb.getCountryID());
				
				insertCity(cityDB);
			}

	}
	
	public Cursor getCode(String cityName) {
		String where = DbHelper.CITY_DB_NAME_EN + " = " + "Ternopil";
		String[] selectionArgs = new String[] { cityName };
		Cursor c = 	db.query(DbHelper.CITY_DB_TABLE, ALL_KEYS, 
				DbHelper.CITY_DB_NAME_EN + "=?", selectionArgs, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

    private long insertCity(CityDB cityDB) {   
        ContentValues values = getCityValues(cityDB);
        return db.insert(DbHelper.CITY_DB_TABLE, null, values);
    }

    private ContentValues getCityValues(CityDB cityDB) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.CITY_DB_ID, cityDB.getCityID());
        values.put(DbHelper.CITY_DB_NAME, cityDB.getName());
        values.put(DbHelper.CITY_DB_NAME_EN, cityDB.getNameEn());
        values.put(DbHelper.CITY_DB_REGION, cityDB.getRegion());
        values.put(DbHelper.CITY_DB_COUNTRY, cityDB.getCountry());
        values.put(DbHelper.CITY_DB_COUNTRY_ID, cityDB.getCountryID());
        return values;
    }
    
	 public Cursor getCursor(String tableName) {	    	
	    	Cursor cursor = db.query(tableName, null, null, null, null, null, null);
	        return cursor;
	    }
    
	public void cleanOldRecords() {
        db.delete(DbHelper.CITY_DB_TABLE, null, null);
    }
	
	public void closeDB() {
        db.close();     
    }

}
