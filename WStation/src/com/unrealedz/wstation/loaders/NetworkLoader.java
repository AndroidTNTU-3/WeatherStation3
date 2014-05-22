package com.unrealedz.wstation.loaders;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import com.unrealedz.wstation.bd.DataCityDbInfoHelper;
import com.unrealedz.wstation.bd.DataCityHelper;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.CitiesDB;
import com.unrealedz.wstation.entity.CityDB;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.parsers.CityDbParser;
import com.unrealedz.wstation.parsers.WeatherParser;


import com.unrealedz.wstation.utils.Contract;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

public class NetworkLoader extends AsyncTask<String, Void, String>{
	
	Object ob;
	
	Context context;
	String nameLocation;
			
	public static interface LoaderCallBack{
		public void setLocationInfo();
		public void setLastUpdate();
		public void setWeekList();
		public void onLoadCityDB();
	}
	
	LoaderCallBack loaderCallBack;
	
	public NetworkLoader(Context context){
		this.context = context;	  
	}
	
	NetworkLoader(){
		
	}

	@Override
	protected String doInBackground(String... params) {
		
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpRequest = null;
		InputStream stream = null;

        httpRequest = new HttpGet(params[1]);
        
        String keyLoader = params[0];
        
        try {
			HttpResponse response = (HttpResponse) client.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			
			stream = entity.getContent();
			try {
				if (keyLoader.equals(Contract.GET_CITY_DB)) {							// Get data and store a database of cities
					
					CitiesDB citiesDB= new CityDbParser().parse(stream);				// call parser for cities DB
				
					DataCityDbInfoHelper dbInfoHelper = new DataCityDbInfoHelper(context);
					DataCityHelper dataCity = new DataCityHelper(context);
					String lastUpdated = dbInfoHelper.getLastUpdated();
		    	  
		    	  if (lastUpdated != ""){		    
		    	  
		    		  if (!lastUpdated.equals((citiesDB).getLastUpdated())) {			//checking if last updated time was updated
		    			  dbInfoHelper.cleanOldRecords();
		    			  dbInfoHelper.insertCitiesDB(citiesDB);
		    			  dataCity.cleanOldRecords();
		    			  dataCity.insertCitiesDB(citiesDB);
		    		  }
		    	  } else{
		    		  dbInfoHelper.cleanOldRecords();
					  dbInfoHelper.insertCitiesDB(citiesDB);
					  dataCity.cleanOldRecords();
					  dataCity.insertCitiesDB(citiesDB);
		    	  }
				  
				  dbInfoHelper.closeDB();
				  dataCity.closeDB();
								
				} else if(keyLoader.equals(Contract.GET_FORECAST)){						// Get data and store to a database of forecast
					
					Forecast forecast = new WeatherParser().parse(stream);				// call parser for forecast DB
					
					DataHelper dh = new DataHelper(context);
					DataDayHelper ddh = new DataDayHelper(context);
					DataWeekHelper dwh = new DataWeekHelper(context);
			      
					dh.cleanOldRecords();
					ddh.cleanOldRecords();
					dwh.cleanOldRecords();
			      
					dh.insertCityItem(forecast);
					ddh.insertDayItem(forecast);
					dwh.insertDayItem(forecast);
					
					dh.closeDB();
					ddh.closeDB();
					dwh.closeDB();
				}
							
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        
		return keyLoader;
	}
	
	@Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      
      if (result.equals(Contract.GET_CITY_DB)){
    	  
    	  if (loaderCallBack != null){
    			 loaderCallBack.onLoadCityDB();			//notification for service(cities data is loaded)
    	  }    	          

      } else if(result.equals(Contract.GET_FORECAST)){
    	  
    	  if (loaderCallBack != null){					//notification for service(forecast data is loaded)
        	  loaderCallBack.setLocationInfo();
        	  loaderCallBack.setLastUpdate();
        	  loaderCallBack.setWeekList();
          } 		  
      }
	}	
	
	public void setLoaderCallBack(LoaderCallBack loaderCallBack) {
		this.loaderCallBack = loaderCallBack;
	}	
	

}
