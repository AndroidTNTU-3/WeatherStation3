package com.unrealedz.wstation;


import com.unrealedz.wstation.LocationLoader.LocationLoaderCallBack;
import com.unrealedz.wstation.NetworkLoader.LoaderCallBack;
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
import com.unrealedz.wstation.utils.UtilsDB;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity implements LoaderCallBack, LoaderCallbacks<Cursor>, LocationLoaderCallBack{
	
	FragmentCurrent fragCurrent;
	FragmentList fragList;
	FragmentInfo fragInfo;
	FragmentTransaction fTrans;
	NetworkLoader nLoader;
	NetworkLoader cLoader;
	LocationLoader locationLoader;
	City city;
	CurrentForecast currentForecast;
	
	
	String url = "http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=uk";
	public static final String CITY_URL = "http://xml.weather.co.ua/1.2/city/?country=804";
	public static final String GET_CITY_DB = "getCityDB";
	public static final String GET_FORECAST = "getForecast";
	
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
        
             
        cityLoad();
        Log.i("DEBUG CUR", "MAin On Activity Created") ;
      //locationLoader = new LocationLoader(context);
	               
    }
    
    private void cityLoad(){
       boolean flag = isOnline(getApplicationContext());
       flag = false;
   		cLoader = new NetworkLoader(this);
   		cLoader.setLoaderCallBack(this);
   		
    	if (isOnline(getApplicationContext())) cLoader.execute(GET_CITY_DB, CITY_URL);

    	//else setLocationInfo();
    }
    
    private void refresh(){
        nLoader = new NetworkLoader(this);
        nLoader.setLoaderCallBack(this); 
    	if (isOnline(getApplicationContext())) nLoader.execute(GET_FORECAST, url);
    }
    
    
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
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
	public void setLocationInfo() {
		
		DataHelper dh = new DataHelper(this);
	    DataDayHelper dd = new DataDayHelper(this);
	    Cursor cursorCity = dh.getCursor(DbHelper.CITY_TABLE);
	    Cursor cursorCurrent = dd.getCursor(DbHelper.CURRENT_DAY_TABLE);
	    if (cursorCity.getCount() !=0 & cursorCurrent.getCount() !=0){
	    city = UtilsDB.getCity(cursorCity);
	    currentForecast = UtilsDB.getCurrentForecast(cursorCurrent);	    
		fragCurrent.setData(city, currentForecast);

	    }
		cursorCity.close();
		cursorCurrent.close();
	}
	
	@Override
	public void setLastUpdate(){
		if (currentForecast != null) fragInfo.setData(currentForecast);
	}
	
	

	@Override
	public void setCurrentDay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWeekList() {
        getLoaderManager().initLoader(0, null, this);
		Log.i("DEBUG CUR", "In setweek") ;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Log.i("DEBUG CUR", "In loader creat") ;
		return new MyCursorLoader(getApplicationContext());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor.getCount() != 0){
		fragList.setCity(city);
		fragList.setCursor(cursor);
		}
	}
	

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadCityDB() {
		Log.i("DEBUG INFO", "In onload:");
		locationLoader = new LocationLoader(getApplicationContext());
		locationLoader.setLoaderCallBack(this);
		locationLoader.getLocation();
	}

	@Override
	public void setLocation(String cityId) {
		Log.i("DEBUG CUR", "COD:" + cityId); 
		url = "http://xml.weather.co.ua/1.2/forecast/" + cityId + "?dayf=5&lang=uk";
		refresh();
	}
	

    
}
