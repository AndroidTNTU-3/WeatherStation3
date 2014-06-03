package com.unrealedz.wstation.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


///////////////////////////////////////
//DB Helper: operation with DB tables//	
///////////////////////////////////////

public class DbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "wbase";
    public static final String CITY_DB_TABLE = "cityDB";
    public static final String CITY_DB_INFO_TABLE = "cityDbInfo";
    public static final String CITY_TABLE = "city";
    public static final String CURRENT_DAY_TABLE = "currentDay";
    public static final String WEEK_TABLE = "week";
    
    public static final String CITY_DB_INFO_VERSION = "cid";
    
    public static final String CITY_DB_ID = "cid";
    public static final String CITY_DB_NAME = "city_name";
    public static final String CITY_DB_NAME_EN = "city_name_en";
    public static final String CITY_DB_REGION = "region";
    public static final String CITY_DB_COUNTRY = "country";
    public static final String CITY_DB_COUNTRY_ID = "country_id";
    
    public static final String CITY_ID = "cid";
    public static final String CITY_NAME = "city_name";
    public static final String CITY_NAME_EN = "city_name_en";
    public static final String REGION = "region";
    public static final String REGION_EN = "region_en";
    public static final String COUNTRY_ID = "country_id";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_EN = "country_en";
    public static final String ID = "_id";
    
    public static final String LAST_UPDATED = "LastUpdated";
    public static final String EXPIRES = "expires";
    public static final String TIME = "time";
    public static final String CLOUD_ID = "cloudId";
    public static final String PICTURE_NAME = "pictureName";
    public static final String TEMPERATURE = "temperature";
    public static final String TEMPERATURE_FLIK = "temperatureFlik";
    public static final String PRESSURE = "pressure";
    public static final String WIND = "wind";
    public static final String WIND_GUST = "windGust";
    public static final String WIND_RUMB = "windRumb";
    public static final String HUMIDITY = "humidity";
    
    public static final String DATE = "date";
    public static final String HOUR = "hour";
    public static final String PPCP = "ppcp";
    public static final String TEMPERATURE_MIN = "temperatureMin";
    public static final String TEMPERATURE_MAX = "temperatureMax";
    public static final String PRESSURE_MIN = "pressureMin";
    public static final String PRESSURE_MAX = "pressureMax";
    public static final String WIND_MIN = "windMin";
    public static final String WIND_MAX = "windMax";
    public static final String HUMIDITY_MIN = "humidityMin";
    public static final String HUMIDITY_MAX = "humidityMax";
    public static final String WPI = "wpi";
    
    public static final String TEMPERATURE_DAY_MIN = "temperatureDayMin";
    public static final String TEMPERATURE_DAY_MAX = "temperatureDayMax";
    
    public static final String CREATE_DB_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + CITY_DB_INFO_TABLE
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + LAST_UPDATED + " TEXT," + CITY_DB_INFO_VERSION + " TEXT);";
    
    public static final String CREATE_CITY_DB_TABLE = "CREATE TABLE IF NOT EXISTS " + CITY_DB_TABLE
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY_DB_ID + " TEXT," + CITY_DB_NAME + " TEXT, "
            + CITY_DB_NAME_EN + " TEXT, " + CITY_DB_REGION + " TEXT, " + CITY_DB_COUNTRY
            + " TEXT, " + CITY_DB_COUNTRY_ID + " INT);";
    
    public static final String CREATE_CITY_TABLE = "CREATE TABLE IF NOT EXISTS " + CITY_TABLE
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY_ID + " TEXT," + CITY_NAME + " TEXT, "
            + CITY_NAME_EN + " TEXT, " + REGION + " TEXT, " + REGION_EN + " TEXT, " + COUNTRY_ID
            + " INT, "  + COUNTRY + " TEXT, " + COUNTRY_EN + " TEXT);";
    
    public static final String CREATE_DAY_TABLE = "CREATE TABLE IF NOT EXISTS " + CURRENT_DAY_TABLE
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + LAST_UPDATED + " TEXT," + EXPIRES + " TEXT, "
            + TIME + " TEXT, " + CLOUD_ID + " INT, " + PICTURE_NAME + " TEXT, " + TEMPERATURE
            + " TEXT, " + TEMPERATURE_FLIK + " TEXT, " + PRESSURE + " INT, " + WIND 
            + " INT, " + WIND_GUST + " INT, " + WIND_RUMB + " INT, " + HUMIDITY + " INT);";
	
    public static final String CREATE_WEEK_TABLE = "CREATE TABLE IF NOT EXISTS " + WEEK_TABLE
            + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " TEXT," + HOUR + " INT, "
            + CLOUD_ID + " INT, " + PICTURE_NAME + " TEXT, " + PPCP + " INT, " + TEMPERATURE_MIN
            + " INT, " + TEMPERATURE_MAX + " INT, " + PRESSURE_MIN + " INT, " + PRESSURE_MAX
            + " INT, " + WIND_MIN + " INT, " + WIND_MAX + " INT, " + WIND_RUMB + " INT, " + HUMIDITY_MIN
            + " INT, " + HUMIDITY_MAX + " INT, " + WPI + " INT);";

	
    private static DbHelper mInstance = null;
    
    public static DbHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
    
   /* public SQLiteDatabase getWritableDatabase() {
    	  SQLiteDatabase sqdb = super.getWritableDatabase();
    	  sqdb.setLockingEnabled(true);
    	  return sqdb;
    	 }*/
    
    private DbHelper(Context context) {
		super(context, DbHelper.DATABASE_NAME, null, DbHelper.DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB_INFO_TABLE);
		db.execSQL(CREATE_CITY_DB_TABLE);
		db.execSQL(CREATE_CITY_TABLE);
		db.execSQL(CREATE_DAY_TABLE);
		db.execSQL(CREATE_WEEK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_DB_INFO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CITY_DB_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CURRENT_DAY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + WEEK_TABLE);
		onCreate(db);
	}

}
