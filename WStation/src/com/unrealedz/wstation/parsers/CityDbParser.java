package com.unrealedz.wstation.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.unrealedz.wstation.entity.CitiesDB;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CityDB;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.entity.ForecastDay;

public class CityDbParser {
	
	private static final String namespace = null;
	
	XmlPullParser xpp;
	XmlPullParserFactory factory;

	
	public CitiesDB parse(InputStream stream)throws XmlPullParserException, IOException{
		
		factory = XmlPullParserFactory.newInstance();
		xpp = factory.newPullParser();
		xpp.setInput(stream, null);
		
		xpp.nextTag();
			xpp.require(XmlPullParser.START_TAG, namespace, "city");

			CitiesDB citiesDB = new CitiesDB();
			
			citiesDB.setLastUpdated(xpp.getAttributeValue(null,
					"last_updated"));
			
			citiesDB.setVersion(xpp.getAttributeValue(null,
					"version"));
			
			List<CityDB> cities = new ArrayList<CityDB>();
			
			while (xpp.next() != XmlPullParser.END_TAG) {
				if (xpp.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = xpp.getName();
				if ("city".equals(name)) {			
					cities.add(readCity(xpp));
				} else {
					skipTag(xpp);
				}
			}

			xpp.require(XmlPullParser.END_TAG, namespace, "city");

			citiesDB.setCityDB(cities);
			Log.i("DEBUG:", String.valueOf(cities.size()));
		return citiesDB;
	}	
	
	private CityDB readCity(XmlPullParser xpp) throws XmlPullParserException, IOException {
		//xpp.require(XmlPullParser.START_TAG, namespace, "city");
		
		CityDB cityDB= new CityDB();
		
		cityDB.setCityID((toInt(xpp.getAttributeValue(null, "id"))));
		
		while (xpp.next() != XmlPullParser.END_TAG){
			
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = xpp.getName();
			if ("name".equals(name)) {
				cityDB.setName(readText(xpp));
			} else if ("name_en".equals(name)) {
				cityDB.setNameEn(readText(xpp));	
			} else if ("region".equals(name)) {
				cityDB.setRegion(readText(xpp));
			} else if ("country".equals(name)) {
				cityDB.setCountry(readText(xpp));
			} else if ("country_id".equals(name)) {
				cityDB.setCountryID(toInt(readText(xpp)));
			} else {
				skipTag(xpp);
			}
		}
		//xpp.require(XmlPullParser.END_TAG, namespace, "city");
		
		return cityDB;
	}
	
	private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
	
	private String readText(XmlPullParser parser)  {
		String result = "";
		try {
			if (parser.next() == XmlPullParser.TEXT) {

				result = parser.getText();

				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	private int toInt(String value) {
		int result = 0;

		try {
			result = Integer.parseInt(value);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
