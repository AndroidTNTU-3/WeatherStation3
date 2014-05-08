package com.unrealedz.wstation;


import com.unrealedz.wstation.LocationLoader.LocationLoaderCallBack;
import com.unrealedz.wstation.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.UpdateService.IUpdateServiceCallBack;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.fragments.FragmentCurrent;
import com.unrealedz.wstation.fragments.FragmentInfo;
import com.unrealedz.wstation.fragments.FragmentList;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.utils.UtilsDB;
import com.unrealedz.wstation.utils.UtilsNet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity implements LoaderCallBack, LocationLoaderCallBack, IUpdateServiceCallBack{
	
	FragmentCurrent fragCurrent;
	FragmentList fragList;
	FragmentInfo fragInfo;
	FragmentTransaction fTrans;
	NetworkLoader nLoader;
	NetworkLoader cLoader;
	LocationLoader locationLoader;
	City city;
	CurrentForecast currentForecast;
	
	ServiceConnection sConn;
	UpdateService updateService;
	boolean bound = false; 			//check if activity connected to service
	boolean isRunning = false;
		
	String url = "http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=uk";

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);               

        fragCurrent = new FragmentCurrent();
        fragList =  new FragmentList();
        fragInfo = new FragmentInfo();
        
        fragCurrent.setRetainInstance(true);
        fragList.setRetainInstance(true); 
        fragInfo.setRetainInstance(true); 
        
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragCurrent, fragCurrent);
        fTrans.add(R.id.fragList, fragList);
        fTrans.add(R.id.fragInfoUpdate, fragInfo);
        fTrans.commit();
        
             
        //cityLoad();
        Log.i("DEBUG CUR", "MAin On Activity Created") ;

        
        sConn = new ServiceConnection() {
        	
        	@Override
			public void onServiceConnected(ComponentName component, IBinder binder) {
		        Log.i("DEBUG", "MainActivity onServiceConnected");
		        updateService = ((UpdateService.ForecastBinder) binder).getService(); 
		        updateService.setOnUpdateServiceCallBack(MainActivity.this);
		        bound = true;		
		        if (isRunning) updateService.cityLoad();  
			}
		      		    
		    public void onServiceDisconnected(ComponentName name) {
		        //Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
		        bound = false;
		    }
			
		 };
		 
		 bindToService();
	               
    }
    
    public void bindToService() {
    	Intent intentService = new Intent(getApplicationContext(), UpdateService.class);

        if (UtilsNet.IsServiceRunning(this)) {
            // Bind to LocalService
        	Log.i("DEBUG:", "Service is running");
            bindService(intentService, sConn, getApplicationContext().BIND_AUTO_CREATE);
    		//playerService.setOnPlayerServiceCallBack(this);
            //if (url != null) playerService.startPlayer(url, nameStation);
            bound = true;
            isRunning = true;
            if (updateService != null)
            updateService.cityLoad();          
        }
        else {
        	//playerIsFirst = true;
        	Log.i("DEBUG:", "Service is not running");
            startService(intentService);
            //updateService.setOnUpdateServiceCallBack(MainActivity.this);
            bindService(intentService, sConn, getApplicationContext().BIND_AUTO_CREATE);
            //playerService.setOnPlayerServiceCallBack(this);
            bound = true;
            //updateService.cityLoad(); 
        }
 
    }
    
    
    // Loader of DataBase cities //
    
    private void cityLoad(){
       boolean flag = UtilsNet.isOnline(getApplicationContext());
       flag = false;
   		cLoader = new NetworkLoader(this);
   		cLoader.setLoaderCallBack(this);
   		
    	if (UtilsNet.isOnline(getApplicationContext())) cLoader.execute(Contract.GET_CITY_DB, Contract.CITY_URL);

    	//else setLocationInfo();
    }
    
    
   // 4 // Loader DataBase forecast //
    
    private void refresh(){
        nLoader = new NetworkLoader(this);
        nLoader.setLoaderCallBack(this); 
    	if (UtilsNet.isOnline(getApplicationContext())) nLoader.execute(Contract.GET_FORECAST, url);
    }
    
    //Listener if a ForecastDB is updated or created
    // Set data forecast into fragments//

	@Override
	public void setLocationInfo() {
		
		DataHelper dh = new DataHelper(this);
	    DataDayHelper dd = new DataDayHelper(this);
	    Cursor cursorCity = dh.getCursor(DbHelper.CITY_TABLE);
	    Cursor cursorCurrent = dd.getCursor(DbHelper.CURRENT_DAY_TABLE);
	    if (cursorCity.getCount() !=0 & cursorCurrent.getCount() !=0){
	    city = UtilsDB.getCity(cursorCity);
	    currentForecast = UtilsDB.getCurrentForecast(cursorCurrent);	    
		fragCurrent.setData(city, currentForecast);
		fragList.setCity(city);
		DataWeekHelper dataWeekHelper = new DataWeekHelper(this);
		Cursor cursor = dataWeekHelper.getTemperatureDay(DbHelper.WEEK_TABLE);
		Log.i("DEBUG CUR", "In setlocatiob: " + cursor.getCount()) ;
		//setWeekList();
	    }
		cursorCity.close();
		cursorCurrent.close();
	}
	
	@Override
	public void setLastUpdate(){
		if (currentForecast != null) fragInfo.setData(currentForecast);
	}
	
	//Get cursor for forecast week for ListView
	
	//Start of the block
	@Override
	public void setWeekList() {
        //getLoaderManager().initLoader(0, null, this);
		Log.i("DEBUG CUR", "In setweek") ;
	}
/*
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Loader<Cursor> loader = null;
		Log.i("DEBUG CUR", "On loader createdd") ;
		loader = new MyCursorLoader(getApplicationContext());
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		Log.i("DEBUG CUR", "On loader finished: " + cursor.getCount()) ;
		if (cursor.getCount() != 0){

		fragList.setCursor(cursor);
		} else onLoaderReset(arg0);
	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}*/
	
	//End of the block
	//Listener when the citiesDB is loaded
	

	@Override
	public void onLocationCurrentPrepared(City city,
			CurrentForecast currentForecast) {
		
		fragCurrent.setData(city, currentForecast);
		fragList.setCity(city);
		
	}
	
	@Override
	public void onLastUpdatePrepared(CurrentForecast currentForecast) {

		if (currentForecast != null) fragInfo.setData(currentForecast);
		
	}
	
	@Override
	public void onForecastPrepared(Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursor.getCount() != 0){
			fragList.setCursor(cursor);
		}
	}
	
	
	
	@Override
	public void onLoadCityDB() {
		Log.i("DEBUG INFO", "In onload:");
		locationLoader = new LocationLoader(getApplicationContext());
		locationLoader.setLoaderCallBack(this);
		locationLoader.getLocation();
	}
	
	
	//Listener when a current city code is get
	@Override
	public void setLocation(String cityId) {
		Log.i("DEBUG CUR", "COD:" + cityId); 
		url = "http://xml.weather.co.ua/1.2/forecast/" + cityId + "?dayf=5&lang=uk";
		refresh();
	}
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
        case R.id.action_refresh:
        	refresh();       	
            return true;       
        default:

        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	  protected void onStop() {
	    super.onStop();
	    if (!bound) return;
	    unbindService(sConn);		//Disconnect from the service
	    bound = false;
	  }

    
}
