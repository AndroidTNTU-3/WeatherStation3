package com.unrealedz.wstation;

import java.util.ArrayList;
import java.util.List;

import com.unrealedz.wstation.bd.DaoDay;
import com.unrealedz.wstation.bd.DaoCityCurrent;
import com.unrealedz.wstation.bd.DaoWeek;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.entity.ForecastDayShort;
import com.unrealedz.wstation.loaders.LocationLoader;
import com.unrealedz.wstation.loaders.NetworkLoader;
import com.unrealedz.wstation.loaders.LocationLoader.LocationLoaderCallBack;
import com.unrealedz.wstation.loaders.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.utils.Utils;
import com.unrealedz.wstation.utils.UtilsDB;
import com.unrealedz.wstation.utils.UtilsNet;

import android.app.AlertDialog;
import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateService extends IntentService implements LoaderCallBack, LocationLoaderCallBack {
		

	public static final String FROM_WIDGET = "com.unrealedz.wstation.FROM_WIDGET";
	private static final String NAME = "UpdateService";	
	private String url = "http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=uk";
	
	public interface IUpdateServiceCallBack {
		//public void onForecastPrepared(Cursor cursor);
		public void onForecastPrepared();
		public void onLocationCurrentPrepared(City city, CurrentForecast currentForecast);
		public void onLastUpdatePrepared(CurrentForecast currentForecast);
	}
	
	
	public UpdateService() {
		super(NAME);
		// TODO Auto-generated constructor stub
	}
	
	IUpdateServiceCallBack usCallBack;
	ForecastBinder binder = new ForecastBinder();
		
	private NetworkLoader nLoader;
	private NetworkLoader cLoader;
	private LocationLoader locationLoader;
	private Context context;
	
	private boolean isWidget = false;
	
	SharedPreferences preferences;
	AppWidgetManager manager;
	ComponentName thisWidget;
	RemoteViews remoteView;
		
	private City city;
	private CurrentForecast currentForecast;
	
	private DaoWeek daoWeek;
		
	@Override  
    public void onCreate()  
    {  
        super.onCreate();  
        Log.i("DEBUG:", "In service on create");      
        
    }  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId)  
    {   		 
        return super.onStartCommand(intent, flags, startId);  
    }
    
    @Override
	protected void onHandleIntent(Intent intent) {
    	context = getApplicationContext();
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

    	if (intent != null) {			 
			String action = intent.getAction();	
			if (FROM_WIDGET.equals(action)) {			//Checking if the service was calling the widget
				
				//Get component for a widget
				manager = AppWidgetManager.getInstance(context);
				thisWidget = new ComponentName(context, ForecastWidget.class);
				remoteView = new RemoteViews(context.getPackageName(), R.layout.widget);
				
				isWidget = true;	
				setLocationInfo();
				setWeekList();
				cityLoad();
			}
    	}

		
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
	    	if (UtilsNet.isOnline(context)) cLoader.execute(Contract.GET_CITY_DB, Contract.CITY_URL);
	}
	


	// 2 //CallBack from NetworkLoader if the citiesDB is loaded
	@Override
	public void onLoadCityDB() {
		locationLoader = new LocationLoader(context);
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
		String language = preferences.getString("language", "uk"); 								//get language forecast
		url = "http://xml.weather.co.ua/1.2/forecast/" + cityId + "?dayf=5&lang=" + language;
		refresh();		
	}
	
	
	// 4 // Loader DataBase forecast //
	private void refresh() {
		nLoader = new NetworkLoader(this);
        nLoader.setLoaderCallBack(this); 
    	if (UtilsNet.isOnline(context)) nLoader.execute(Contract.GET_FORECAST, url);
	}
	
	// 5 // Get object of city location and current forecast //
	//      Send data to activity fragments and widget       // 
	@Override
	public void setLocationInfo() {
		
		DaoCityCurrent daoCityCurrent = new DaoCityCurrent(this);
		DaoDay daoDay = new DaoDay(this);
	    	    
	    city = daoCityCurrent.getCity();
	    currentForecast = daoDay.getCurrentForecast();
	    
	    if (city != null && currentForecast != null){
 
	    	if (!isWidget){														//if it activity - sent data to MainActivity
	    		if (UtilsNet.isRunning(context)){
	    			usCallBack.onLocationCurrentPrepared(city, currentForecast);	
	    		}
	    	} else sendInfoWidget(city, currentForecast);						//if widget - sent data to widget
	    } else Log.i("DEBUG:", "Is null"); 
	    daoCityCurrent.closeDb();
	    
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
		
			if (!isWidget){														//if it activity - sent data to MainActivity
	    		if (UtilsNet.isRunning(context)){
	    			usCallBack.onForecastPrepared();	
	    		}
	    	} else setWeekToWidget();										//if widget - sent data to widget

	}
	
	// 6 // Send current forecast data to views of widget //
	private void sendInfoWidget(City city, CurrentForecast currentForecast){
		 
		 remoteView.setTextViewText(R.id.tvwCity, city.getName());
		 remoteView.setTextViewText(R.id.tvwRegion, city.getCountry().getCountry());
		 remoteView.setTextViewText(R.id.tvwCloud, Utils.getCloud(currentForecast.getCloudId(), context));
		 remoteView.setTextViewText(R.id.tvwTemperatura, currentForecast.getTemperature() + "°");
		 remoteView.setTextViewText(R.id.tvwTemperaturaFlik, currentForecast.getTemperatureFlik() + "°");
		 String pictureName =  currentForecast.getPictureName();
		 remoteView.setImageViewResource(R.id.ivwCurrent, Utils.getWidgetImageId(pictureName, context));
		 manager.updateAppWidget(thisWidget, remoteView);
	 }	

	// 7 // Send week forecast data to views of widget //
	public void setWeekToWidget(){
				 
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
		 
		 String tempMinMax = "";
		 String weekDay = "";
		 String pictureName = "";
		 
		 daoWeek = new DaoWeek(this);
		
		List<ForecastDayShort> forecastDaysShort = daoWeek.getShortWeek();
		if (forecastDaysShort != null) {
			
			for(int i = 0; i < forecastDaysShort.size(); i++){
				ForecastDayShort fds = forecastDaysShort.get(i);
				tempMinMax = String.valueOf(fds.getTemperatureMin()) + "°/" + String.valueOf(fds.getTemperatureMax()) +  "°";
				weekDay = Utils.getStringDayWeekShort(fds.getDate());
				pictureName = fds.getPictureName();
				remoteView.setTextViewText(listTextView.get(i), weekDay);
				remoteView.setTextViewText(listTextViewTemp.get(i), tempMinMax);
				remoteView.setImageViewResource(imageViewWeekDay.get(i), Utils.getImageId(pictureName, context));
			}		
			
		}
		 manager.updateAppWidget(thisWidget, remoteView);
		 daoWeek.closeDb();
	}
	
	//registered observers
	public void setOnUpdateServiceCallBack(IUpdateServiceCallBack usCallBack) {		
		this.usCallBack = usCallBack;
	}

	
	
	public void onDestroy() {
	    super.onDestroy();
	}
	
}
