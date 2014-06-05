package com.unrealedz.wstation.loaders;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.unrealedz.wstation.factory.FactoryLoader;

import com.unrealedz.wstation.utils.Contract;

import android.content.Context;
import android.os.AsyncTask;

public class NetworkLoader extends AsyncTask<String, Void, String>{
		
	Context context;
			
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
	
	
	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 * @param  params[1]  an absolute URL 
	 * @param  params[0]  a flag for getting necessary factory
	 */

	@Override
	protected String doInBackground(String... params) {
		String keyLoader = params[0]; 
		int key = 0;
		
		if (keyLoader.equals(Contract.GET_CITY_DB)) key = FactoryLoader.CITY;
		else if (keyLoader.equals(Contract.GET_FORECAST)) key = FactoryLoader.FORECAST;
		
				FactoryLoader factory = FactoryLoader.getFactoryLoader(key);
				factory.getStream(params[1]);
				try {
					
					factory.processing(context);
					
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
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
