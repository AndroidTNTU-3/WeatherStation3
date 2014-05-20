package com.unrealedz.wstation;

import java.util.ArrayList;
import java.util.List;

import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.loaders.LocationLoader;
import com.unrealedz.wstation.loaders.NetworkLoader;
import com.unrealedz.wstation.loaders.LocationLoader.LocationLoaderCallBack;
import com.unrealedz.wstation.loaders.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.utils.Utils;
import com.unrealedz.wstation.utils.UtilsDB;
import com.unrealedz.wstation.utils.UtilsNet;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateService extends IntentService implements LoaderCallBack, LocationLoaderCallBack {
		
	private static final String NAME = "UpdateService";	
	public static final String FROM_WIDGET = "com.unrealedz.wstation.FROM_WIDGET";
	String url = "http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=uk";
	
	public interface IUpdateServiceCallBack {
		public void onForecastPrepared(Cursor cursor);
		public void onLocationCurrentPrepared(City city, CurrentForecast currentForecast);
		public void onLastUpdatePrepared(CurrentForecast currentForecast);
	}
	
	
	public UpdateService() {
		super(NAME);
		// TODO Auto-generated constructor stub
	}
	
	IUpdateServiceCallBack usCallBack;
	ForecastBinder binder = new ForecastBinder();
		
	NetworkLoader nLoader;
	NetworkLoader cLoader;
	LocationLoader locationLoader;
	Context context;
	
	boolean isWidget = false;
		
	City city;
	CurrentForecast currentForecast;
	
	DataWeekHelper dataWeekHelper;
	DataHelper dh;
	DataDayHelper dd;
	
	SharedPreferences preferences;
	
	@Override  
    public void onCreate()  
    {  
        super.onCreate();  
        Log.i("DEBUG:", "In service on create");      
        
    }  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId)  
    {
    	preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	
    	if (intent != null) {			 
			String action = intent.getAction();	
			if (FROM_WIDGET.equals(action)) {			//Checking if the service was calling the widget  
				isWidget = true;	
				setLocationInfo();
				setWeekList();
				cityLoad();
			}
    	}
    	context = getApplicationContext();  	 
       // cityLoad();
        return super.onStartCommand(intent, flags, startId);  
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}
	
	public class ForecastBinder extends Binder {
        /**
         * Returns the instance of this service for a client to make method calls on it.
         * @return the instance of this service.
         */
        public UpdateService getService() {
            return UpdateService.this;
        }
 
    }
 		
    // 1 // Loader of DataBase cities //
	public void cityLoad(){
	   		cLoader = new NetworkLoader(this);
	   		cLoader.setLoaderCallBack(this);
	    	if (UtilsNet.isOnline(getApplicationContext())) cLoader.execute(Contract.GET_CITY_DB, Contract.CITY_URL);
	}
	


	// 2 //CallBack from NetworkLoader if the citiesDB is loaded
	@Override
	public void onLoadCityDB() {
		locationLoader = new LocationLoader(getApplicationContext());
		locationLoader.setLoaderCallBack(this);
    	String cityIdManual = preferences.getString("cityId", "");
    	
    	if (cityIdManual.equalsIgnoreCase("")) locationLoader.getLocation();	// check if a was set manual location 
    	else { 
    		setLocation(cityIdManual);
    	}
    	
	}
	
	
	// 3 //CallBack from LocationLoader if a current city code is get
	@Override
	public void setLocation(String cityId) {
		//int cityId = 23; //Kyiv code from dummy 
		url = "http://xml.weather.co.ua/1.2/forecast/" + cityId + "?dayf=5&lang=uk";
		refresh();		
	}
	
	
	// 4 // Loader DataBase forecast //
	private void refresh() {
		nLoader = new NetworkLoader(this);
        nLoader.setLoaderCallBack(this); 
    	if (UtilsNet.isOnline(getApplicationContext())) nLoader.execute(Contract.GET_FORECAST, url);
	}
	
	// 5 // Get object of city location and current forecast //
	//      Send data to activity fragments and widget       // 
	@Override
	public void setLocationInfo() {
		
		dh = new DataHelper(this);
	    dd = new DataDayHelper(this);
	    
	    Cursor cursorCity = dh.getCursor(DbHelper.CITY_TABLE);
	    Cursor cursorCurrent = dd.getCursor(DbHelper.CURRENT_DAY_TABLE);

		
	    if (cursorCity.getCount() !=0 & cursorCurrent.getCount() !=0){
	    	city = UtilsDB.getCity(cursorCity);
	    	currentForecast = UtilsDB.getCurrentForecast(cursorCurrent);
	    	
	    	if (!isWidget){														//if it activity - sent data to MainActivity
	    		if (UtilsNet.isRunning(context)){
	    			usCallBack.onLocationCurrentPrepared(city, currentForecast);	
	    		}
	    	} else sendInfoWidget(city, currentForecast);						//if widget - sent data to widget
	    }
	    
	    cursorCity.close();		
	    cursorCurrent.close();

	    
	}
	
	// 5.1 // Send last update info to activity fragments //
	
	@Override
	public void setLastUpdate() {
		if (currentForecast != null){
			if (!isWidget){														//if it activity - sent data to MainActivity
	    		if (UtilsNet.isRunning(context)){
	    			usCallBack.onLastUpdatePrepared(currentForecast);	
	    		}
	    	}				
		}
	}
	
	// 5.2 // Get cursor week forecast                    //
	//        Send data to activity fragments and widget  // 
	
	@Override
	public void setWeekList() {
		dataWeekHelper = new DataWeekHelper(this);
		Cursor cursor = dataWeekHelper.getTemperatureDay(DbHelper.WEEK_TABLE);
		
		if (cursor.getCount() !=0){
			if (!isWidget){														//if it activity - sent data to MainActivity
	    		if (UtilsNet.isRunning(context)){
	    			usCallBack.onForecastPrepared(cursor);	
	    		}
	    	} else setWeekToWidget(cursor);										//if widget - sent data to widget
		}

	}
	
	// 6 // Send current forecast data to views of widget //
	private void sendInfoWidget(City city, CurrentForecast currentForecast){
		 
		 AppWidgetManager manager = AppWidgetManager.getInstance(this.getApplicationContext());
		 ComponentName thisWidget = new ComponentName(this.getApplicationContext(), ForecastWidget.class);
		 
		 RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
		 remoteView.setTextViewText(R.id.tvwCity, city.getName());
		 remoteView.setTextViewText(R.id.tvwRegion, city.getRegion().getRegion());
		 remoteView.setTextViewText(R.id.tvwCloud, Utils.getCloud(currentForecast.getCloudId(), getApplicationContext()));
		 remoteView.setTextViewText(R.id.tvwTemperatura, currentForecast.getTemperature() + "°");
		 remoteView.setTextViewText(R.id.tvwTemperaturaFlik, currentForecast.getTemperatureFlik() + "°");
		 remoteView.setTextViewText(R.id.tvTempMinMaxDay1, "KOW");
		 String pictureName =  currentForecast.getPictureName();
		 remoteView.setImageViewResource(R.id.ivwCurrent, Utils.getNormalImageId(pictureName, getApplicationContext()));
		 manager.updateAppWidget(thisWidget, remoteView);
	 }	

	// 7 // Send week forecast data to views of widget //
	public void setWeekToWidget(Cursor cursor){
		
		 AppWidgetManager manager = AppWidgetManager.getInstance(this.getApplicationContext());
		 ComponentName thisWidget = new ComponentName(this.getApplicationContext(), ForecastWidget.class);
		 
		 RemoteViews remoteView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);
		 
		 List<Integer> listTextView = new ArrayList<Integer>();
		
		 listTextView.add(R.id.tvDay1);
		 listTextView.add(R.id.tvDay2);
		 listTextView.add(R.id.tvDay3);
		 listTextView.add(R.id.tvDay4);
		 listTextView.add(R.id.tvDay5);
		 
		 List<Integer> listTextViewTemp = new ArrayList<Integer>();
			
		 listTextViewTemp.add(R.id.tvTempMinMaxDay1);
		 listTextViewTemp.add(R.id.tvTempMinMaxDay2);
		 listTextViewTemp.add(R.id.tvTempMinMaxDay3);
		 listTextViewTemp.add(R.id.tvTempMinMaxDay4);
		 listTextViewTemp.add(R.id.tvTempMinMaxDay5);
		 
		 List<Integer> imageViewWeekDay = new ArrayList<Integer>();
			
		 imageViewWeekDay.add(R.id.imageViewDay1);
		 imageViewWeekDay.add(R.id.imageViewDay2);
		 imageViewWeekDay.add(R.id.imageViewDay3);
		 imageViewWeekDay.add(R.id.imageViewDay4);
		 imageViewWeekDay.add(R.id.imageViewDay5);
		 
		 String tempMainMax = "";
		 String weekDay = "";
		 String pictureName = "";
		 
		for(int i = 0; i < imageViewWeekDay.size(); i++){
			 tempMainMax = String.valueOf(cursor.getInt(cursor.getColumnIndex(DbHelper.TEMPERATURE_MIN))) + "°/" + 
			 			String.valueOf(cursor.getInt(cursor.getColumnIndex(DbHelper.TEMPERATURE_MAX))) + "°";
			 weekDay = Utils.getStringDayWeekShort(cursor.getString(cursor.getColumnIndex(DbHelper.DATE)));
			 pictureName =  cursor.getString(cursor.getColumnIndex(DbHelper.PICTURE_NAME));
			 remoteView.setTextViewText(listTextView.get(i), weekDay);
			 remoteView.setTextViewText(listTextViewTemp.get(i), tempMainMax);
			 remoteView.setImageViewResource(imageViewWeekDay.get(i), Utils.getImageId(pictureName, getApplicationContext()));
			 cursor.moveToNext();
		 }		 				 
		 manager.updateAppWidget(thisWidget, remoteView);
	}
	
	//registered observers
	public void setOnUpdateServiceCallBack(IUpdateServiceCallBack usCallBack) {		
		this.usCallBack = usCallBack;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}
	
	public void onDestroy() {
	    super.onDestroy();
		dd.closeCursorGetCursor();
		dh.closeCursorGetCursor();
	    dh.closeDB();
	    dd.closeDB();
		dataWeekHelper.closecursorGetTemperatureDay();
	    dataWeekHelper.closeDB();
	  }
	
	

}
