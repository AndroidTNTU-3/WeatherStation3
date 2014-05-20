package com.unrealedz.wstation.bd;

/////////////////////////////////////////
//Helper: info of current city location//
/////////////////////////////////////////

import java.util.List;

import com.unrealedz.wstation.entity.CitiesDB;
import com.unrealedz.wstation.entity.CityDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataCityHelper {
	
	private SQLiteDatabase db;
	private List<CityDB> cities;
	
	Cursor cursorGetCode;
	Cursor cursorGetLocation;
	Cursor cursorGetCodeFromId;
	Cursor cursorGetCursor;
	
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
		//String where = DbHelper.CITY_DB_NAME_EN + " = " + "Ternopil";
		String[] selectionArgs = new String[] { cityName };
		cursorGetCode = 	db.query(DbHelper.CITY_DB_TABLE, ALL_KEYS, 
				DbHelper.CITY_DB_NAME_EN + "=?", selectionArgs, null, null, null, null);
		if (cursorGetCode != null) {
			cursorGetCode.moveToFirst();
		}
		return cursorGetCode;
	}
	
	public Cursor getLocation(String cityName) {
		//String where = DbHelper.CITY_DB_NAME_EN + " = " + "Ternopil";
		String[] selectionArgs = new String[] { cityName };
		cursorGetLocation = 	db.query(DbHelper.CITY_DB_TABLE, null, 
				DbHelper.CITY_DB_NAME_EN + "=?", selectionArgs, null, null, null, null);
		if (cursorGetLocation != null) {
			cursorGetLocation.moveToFirst();
		}
		return cursorGetLocation;
	}
	
	public Cursor getCodeFromId(int id) {
		//String where = DbHelper.CITY_DB_NAME_EN + " = " + "Ternopil";
		String[] selectionArgs = new String[] { String.valueOf(id) };
		Cursor cursorGetCodeFromId = 	db.query(DbHelper.CITY_DB_TABLE, null, 
				"_id" + "=?", selectionArgs, null, null, null, null);
		if (cursorGetCodeFromId != null) {
			cursorGetCodeFromId.moveToFirst();
		}
		return cursorGetCodeFromId;
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
		 cursorGetCursor = db.query(tableName, null, null, null, null, null, null);
	        return cursorGetCursor;
	    }
    
	public void cleanOldRecords() {
        db.delete(DbHelper.CITY_DB_TABLE, null, null);
    }
	
	
	public void closeCursorGetCode() {
		if(cursorGetCode != null) cursorGetCode.close();     
    }
	
	public void closeCursorGetLocation() {
		if (cursorGetLocation != null)cursorGetLocation.close();     
    }
	
	public void closeCursorGetCodeFromId() {
		if (cursorGetCodeFromId != null) cursorGetCodeFromId.close();     
    }
	
	public void closeCursorGetCursor() {
		if (cursorGetCursor != null) cursorGetCursor.close();     
    }
	
	public void closeDB() {
        db.close();     
    }

}
