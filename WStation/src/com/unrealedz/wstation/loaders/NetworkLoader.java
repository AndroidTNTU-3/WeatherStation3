package com.unrealedz.wstation.loaders;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParserException;

import com.unrealedz.wstation.factory.FactoryLoader;
import com.unrealedz.wstation.utils.Contract;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class NetworkLoader extends AsyncTask<String, Void, InputStream>{
		
	Context context;
	private InputStream stream;
	private FactoryLoader factory;
	public static interface LoaderCallBack{
		public void weatherServiseNotAvalable();
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
	protected InputStream doInBackground(String... params) {
		String keyLoader = params[0]; 
		int key = 0;
		
		if (keyLoader.equals(Contract.GET_CITY_DB)) key = FactoryLoader.CITY;
		else if (keyLoader.equals(Contract.GET_FORECAST)) key = FactoryLoader.FORECAST;
		
				factory = FactoryLoader.getFactoryLoader(key);
				stream = factory.getStream(params[1]);
				try {
					if(stream != null)
						factory.processing(context);					
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}	
		return stream;
	}
		
	
	@Override
    protected void onPostExecute(InputStream result) {
      super.onPostExecute(result);
      if (loaderCallBack != null){
	      if(result != null){	      
		    	  factory.successfullyNitification(loaderCallBack);			//notification for the service(a cities data is loaded)
	      }else loaderCallBack.weatherServiseNotAvalable();	
      }      
	}	
	
	public void setLoaderCallBack(LoaderCallBack loaderCallBack) {
		this.loaderCallBack = loaderCallBack;
	}	
	

}
