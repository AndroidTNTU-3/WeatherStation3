package com.unrealedz.wstation.loaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.unrealedz.wstation.bd.DataCityHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DbHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationLoader implements LocationListener {
	
	public static interface LocationLoaderCallBack{
		public void setLocation(String cityId);
	}
	
	LocationLoaderCallBack locationLoaderCallBack;
	
	  private LocationManager locationManager;
	  private String provider;
	  private Context context;
	  private Location location;
	  double latitude;
	  double longitude; 
	  private SharedPreferences preferences;
	  	
	public LocationLoader(Context context){
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);

	}
	
	private void setProvider(String providers) {
		locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);	
		if (providers.equals("GPS")) provider = locationManager.GPS_PROVIDER;
		else if (providers.equals("Network")) provider = locationManager.NETWORK_PROVIDER;
		else if (providers.equals("Best provider")){
			Criteria criteria = new Criteria();
		    provider = locationManager.getBestProvider(criteria, false);
		}
	}

	public void getLocation(){
		 // Get the location manager
		
		setProvider(preferences.getString("providers", "Best provider"));   
	    locationManager.requestLocationUpdates(provider, 0, 0, this);
	    
	    if (locationManager != null) {
	    	 
            location = locationManager
                    .getLastKnownLocation(provider);
            if (location != null) {
            	latitude = location.getLatitude();
            	longitude = location.getLongitude();
            	
        	    Log.i("DEBUG", " lat:" + String.valueOf(location.getLatitude()));
        	    Log.i("DEBUG", " lon:" + String.valueOf(location.getLongitude()));
        	    
        	    new GeoCoderLoader(context).execute(latitude, longitude);        
            }    
	    }
	    return;
	}
	
	public void LocationUpdate(){
		locationManager.requestLocationUpdates(provider, 0, 0, this);
	}
	

	@Override
	public void onLocationChanged(Location arg0) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
	
	private class GeoCoderLoader extends AsyncTask<Double, Void, String> {
		
		Context context;
		
		GeoCoderLoader(Context context){
			this.context = context;
		}

		@Override
		protected String doInBackground(Double... params) {

			List<Address> addresses = new ArrayList<Address>();
		
			String cityName = "";
			Geocoder geoCoder = new Geocoder(context, Locale.ENGLISH);
			
			if (geoCoder != null){
			
			try {
				addresses = geoCoder.getFromLocation(params[0], params[1], 1);
			} catch (IOException e) {
				e.printStackTrace();
			}			
			
			for (Address adrs : addresses) {
	            if (adrs != null) {

	            	String city = adrs.getLocality();
	                if (city != null && !city.equals("")) {
	                    cityName = city;	                    
	                } else {

	                }
	                // // you should also try with addresses.get(0).toSring();

	            }
			}
			}					
			return cityName;
		}
		
		@Override
	    protected void onPostExecute(String result) {
	      super.onPostExecute(result);
	      getCode(result);
		}
		
	}

	//get city code from cities DB
	public String getCode(String nameLocation) {
		
		String codeLocation;
		DataCityHelper dch = new DataCityHelper(context);
		if(nameLocation == "") nameLocation = "Kiev";		//dummy for emulator
		
		/*Cursor cursorCityDB = dch.getCode(nameLocation);
		String newCodeLocation = cursorCityDB.getString(cursorCityDB.getColumnIndex(DbHelper.CITY_DB_ID));*/
		String newCodeLocation = dch.getNewCode(nameLocation);
		
		Log.i("DEBUG", " locationcode:" + newCodeLocation);
		DataHelper dh = new DataHelper(context);
		Cursor cursorCity = dh.getCursor(DbHelper.CITY_TABLE);	
		
		//checking if your location was changed 
		if (cursorCity.getCount() != 0){
			cursorCity.moveToFirst();		
			String oldCodeLocation = cursorCity.getString(cursorCity.getColumnIndex(DbHelper.CITY_ID));
				
			if (newCodeLocation != oldCodeLocation){
				codeLocation = newCodeLocation;
			} else codeLocation = oldCodeLocation;	
		} else codeLocation = newCodeLocation;

	    locationLoaderCallBack.setLocation(codeLocation); // sent code location to service	    
	    
	    //cursorCityDB.close();
	    cursorCity.close();
	    dh.closeCursorGetCursor();
	    dh.closeDB();
	    dch.closeCursorGetCode();
	    dch.closeDB();
		return codeLocation;
	}
	
	public void setLoaderCallBack(LocationLoaderCallBack locationLoaderCallBack) {
		this.locationLoaderCallBack = locationLoaderCallBack;
	}

}
