package com.unrealedz.wstation.bd;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unrealedz.wstation.entity.CitiesDB;

/////////////////////////////////////////////////////////
//Info of version and last updated time cities database//
/////////////////////////////////////////////////////////

public class DaoCityDbVersion extends BaseDao{
	

	private CitiesDB citiesDB;
	private Cursor cursor;
	
	public DaoCityDbVersion(Context context) {
		super(context);
        /*openHelper = new DbHelper(context);
        db = openHelper.getWritableDatabase();*/
		openDb();
       
    }
	
	public void cleanOldFeeds() {
        db.delete(DbHelper.CITY_DB_INFO_TABLE, null, null);
    }
	
	public void insertCitiesDB(CitiesDB citiesDB){
		this.citiesDB = citiesDB;							
			insertCity(citiesDB);

	}

    private long insertCity(CitiesDB citiesDB) {   
        ContentValues values = getCityValues(citiesDB);
        return db.insert(DbHelper.CITY_DB_INFO_TABLE, null, values);
    }

    private ContentValues getCityValues(CitiesDB citiesDB) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.LAST_UPDATED, citiesDB.getLastUpdated());
        values.put(DbHelper.CITY_DB_INFO_VERSION, citiesDB.getVersion());
        return values;
    }
    
	 public Cursor getCursor(String tableName) {	    	
	    	cursor = db.query(tableName, null, null, null, null, null, null);
	        return cursor;
	 }
    
	 public String getLastUpdated(){
			 
		 String lastUpdated = "";
	    	Cursor cursor = db.query(DbHelper.CITY_DB_INFO_TABLE, null, null, null, null, null, null);
	    	
	    	if (cursor.getCount() !=0){
	    		cursor.moveToFirst();		
	    		lastUpdated = cursor.getString(cursor.getColumnIndex(DbHelper.LAST_UPDATED));
	        }
	    	
	    	if(cursor != null) cursor.close();
	    	
			return lastUpdated;
		 
	 }
	 
	public void cleanOldRecords() {
        db.delete(DbHelper.CITY_DB_INFO_TABLE, null, null);
    }

}
