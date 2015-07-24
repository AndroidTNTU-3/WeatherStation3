package com.unrealedz.wstation.factory;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.unrealedz.wstation.bd.DaoCityCurrent;
import com.unrealedz.wstation.bd.DaoDay;
import com.unrealedz.wstation.bd.DaoWeek;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.loaders.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.parsers.WeatherParser;

public class FactoryWeatherParser extends FactoryLoader {

	@Override
	public void processing(Context context) {
	
		Forecast forecast;
		try {
			forecast = new WeatherParser().parse(stream);
			
			DaoCityCurrent cityCurrent  = new DaoCityCurrent(context);
			DaoDay daoDay = new DaoDay(context);
			DaoWeek daoWeek = new DaoWeek(context);
	      
			cityCurrent.cleanOldRecords();
			daoDay.cleanOldRecords();
			daoWeek.cleanOldRecords();
	      
			cityCurrent.insertCityItem(forecast);
			daoDay.insertDayItem(forecast);
			daoWeek.insertDayItem(forecast);

			//daoWeek.closeDb();
			
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
	
	@Override
	public void successfullyNitification(LoaderCallBack loaderCallBack){
		loaderCallBack.setLocationInfo();
   	  	loaderCallBack.setLastUpdate();
   	  	loaderCallBack.setWeekList();
	}

}
