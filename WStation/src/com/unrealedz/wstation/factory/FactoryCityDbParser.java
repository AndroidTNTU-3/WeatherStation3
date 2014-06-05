package com.unrealedz.wstation.factory;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.unrealedz.wstation.bd.DaoCityDb;
import com.unrealedz.wstation.bd.DaoCityDbVersion;
import com.unrealedz.wstation.entity.CitiesDB;
import com.unrealedz.wstation.parsers.CityDbParser;

public class FactoryCityDbParser extends FactoryLoader{
	
	DaoCityDbVersion cityDbVersion = null;
	DaoCityDb daoCity = null;
	CitiesDB citiesDB = null;
	
	@Override
	public void processing(Context context){

		
		try {
			citiesDB = new CityDbParser().parse(stream);
			
			cityDbVersion = new DaoCityDbVersion(context);
			daoCity = new DaoCityDb(context);
			String lastUpdated = cityDbVersion.getLastUpdated();
			
			if (lastUpdated != ""){		    
		    	  
	    		  if (!lastUpdated.equals((citiesDB).getLastUpdated())) {			//checking if last updated time was updated
	    			  storeToDb();

	    		  }
	    	  } else{
	    		  storeToDb();

	    	  }
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void storeToDb(){
		cityDbVersion.cleanOldRecords();
		cityDbVersion.insertCitiesDB(citiesDB);

		daoCity.cleanOldRecords();
		daoCity.insertCitiesDB(citiesDB);
	}

}
