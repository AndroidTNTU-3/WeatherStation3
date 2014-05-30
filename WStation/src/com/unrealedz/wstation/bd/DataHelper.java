package com.unrealedz.wstation.bd;

/////////////////////////////////
//Helper: info of location city//
//for save to cities DB		   //	
/////////////////////////////////

import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.City.Country;
import com.unrealedz.wstation.entity.City.Region;
import com.unrealedz.wstation.entity.Forecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataHelper {
	
	private SQLiteDatabase db;
	private Cursor cursor;
	private DbHelper openHelper;	
	public DataHelper(Context context) {
       openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();
       /* openHelper = DbHelper.getInstance(context);
        db = openHelper.getWritableDatabase();*/
       
    }
	
	public long insertCityItem(Forecast forecast) {
		City city = new City();
		city.setId(forecast.getCity().getId());
		city.setName(forecast.getCity().getName());
		city.setNameEn(forecast.getCity().getNameEn());
		city.setRegion(readRegion(forecast.getCity().getRegion()));
		city.setCountry(readCountry(forecast.getCity().getCountry()));
        return insertCity(city);
    }

	private Region readRegion(Region reg) {
    	Region region = new Region();
    	region.setRegion(reg.getRegion());
    	region.setRegionEn(reg.getRegionEn());
		return region;
	}
	
    private Country readCountry(Country con) {
		Country country = new Country();
		country.setCountryId(con.getCountryId());
		country.setCountry(con.getCountry());
		country.setCountryEn(con.getCountryEn());
		return country;
	}

	public void cleanOldFeeds() {
        db.delete(DbHelper.CITY_TABLE, null, null);
    }

    private long insertCity(City city) {
       Log.i("DEBUG DB", "insertFeed");
        ContentValues values = getCityValues(city);
        return db.insert(DbHelper.CITY_TABLE, null, values);
    }

    private ContentValues getCityValues(City city) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.CITY_ID, city.getId());
        values.put(DbHelper.CITY_NAME, city.getName());
        values.put(DbHelper.CITY_NAME_EN, city.getNameEn());
        values.put(DbHelper.REGION, city.getRegion().getRegion());
        values.put(DbHelper.REGION_EN, city.getRegion().getRegionEn());
        values.put(DbHelper.COUNTRY, city.getCountry().getCountry());
        values.put(DbHelper.COUNTRY_EN, city.getCountry().getCountryEn());
        return values;
    }
    
	public void cleanOldRecords() {
        db.delete(DbHelper.CITY_TABLE, null, null);
    }
    
    
    public Cursor getCursor(String tableName) {
    	
    	cursor = db.query(tableName, null, null, null, null, null, null);
        return cursor;
    }
    
    
    //Get city info
	public City getCity() {

		City city = null;
		Country country = new Country();
		Region region = new Region();

		Cursor cursor = db.query(DbHelper.CITY_TABLE, null, null, null, null, null, null);
		
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			city = new City();
			city.setId(cursor.getInt(cursor.getColumnIndex(DbHelper.ID)));
			city.setName(cursor.getString(cursor
					.getColumnIndex(DbHelper.CITY_NAME)));
			city.setNameEn(cursor.getString(cursor
					.getColumnIndex(DbHelper.CITY_NAME_EN)));
			country.setCountryId(cursor.getInt(cursor
					.getColumnIndex(DbHelper.COUNTRY_ID)));
			country.setCountry(cursor.getString(cursor
					.getColumnIndex(DbHelper.COUNTRY)));
			country.setCountryEn(cursor.getString(cursor
					.getColumnIndex(DbHelper.COUNTRY_EN)));
			city.setCountry(country);
			region.setRegion(cursor.getString(cursor
					.getColumnIndex(DbHelper.REGION)));
			region.setRegionEn(cursor.getString(cursor
					.getColumnIndex(DbHelper.REGION_EN)));
			city.setRegion(region);
		}
		
		if(cursor != null) cursor.close();
		
		return city;

	}
    
    public String getOldCodeLocation() {   
    	String oldCodeLocation = "";
    	Cursor cursor = db.query(DbHelper.CITY_TABLE, null, null, null, null, null, null);
    	
    	if (cursor.getCount() !=0){
    		cursor.moveToFirst();		
    		oldCodeLocation = cursor.getString(cursor.getColumnIndex(DbHelper.CITY_ID));
        }
    	
    	if(cursor != null) cursor.close();
    	
		return oldCodeLocation;
    }
    
	public void closeDB() {
		openHelper.close();    
    }
}
