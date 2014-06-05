package com.unrealedz.wstation.factory;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public abstract class FactoryLoader {
	
	protected InputStream stream;
	
	public static final int CITY = 0;
	public static final int FORECAST = 1;
	
	public static FactoryLoader getFactoryLoader(int keyLoader){
		switch(keyLoader){
		case CITY: 
			return new FactoryCityDbParser();
		case FORECAST:
			return new FactoryWeatherParser();	
		}
		return null;
	}
		
	/*
	 * Load stream with URL
	 */
	
	public void getStream(String url){
		
		HttpClient client = new DefaultHttpClient();
		
		HttpGet httpRequest = null;
		stream = null;

        httpRequest = new HttpGet(url);
        
        HttpResponse response;
		try {
			
			response = (HttpResponse) client.execute(httpRequest);
			HttpEntity entity = response.getEntity();		
			stream = entity.getContent();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	 * Abstract method for
	 * process parse a data from a stream and store in DataBase
	 */
	
	public abstract void processing(Context context) throws XmlPullParserException, IOException;

}
