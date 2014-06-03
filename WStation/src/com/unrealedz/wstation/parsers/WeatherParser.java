package com.unrealedz.wstation.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.unrealedz.wstation.bd.DaoCityDbVersion;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.entity.Forecast;
import com.unrealedz.wstation.entity.City.Country;
import com.unrealedz.wstation.entity.City.Region;
import com.unrealedz.wstation.entity.ForecastDay;

public class WeatherParser {
	
	private static final String TAG_NAME = "name";
	private static final String TAG_NAME_EN = "name_en";
	private static final String TAG_REGION = "region";
	private static final String TAG_COUNTRY = "country";
	private static final String TAG_CITY = "url";
	private static final String TAG_STATUS = "status";
	private static final String TAG_MESSAGE = "message";
	private static final String namespace = null;
	
	Region region;
	boolean flag_city = false;
	boolean flag_region = false;
	boolean flag_country = false;
	XmlPullParser xpp;
	XmlPullParserFactory factory;
	

	
	public Forecast parse(InputStream stream)throws XmlPullParserException, IOException {
		
			
			factory = XmlPullParserFactory.newInstance();
			xpp = factory.newPullParser();
			xpp.setInput(stream, null);
			xpp.nextTag();
				xpp.require(XmlPullParser.START_TAG, namespace, "forecast");

				Forecast forecast = new Forecast();

				forecast.setLastUpdated(xpp.getAttributeValue(null,
						"last_updated"));

				while (xpp.next() != XmlPullParser.END_TAG) {
					if (xpp.getEventType() != XmlPullParser.START_TAG) {
						continue;
					}
					Log.i("DEBUG: ", xpp.getName());
					String name = xpp.getName();
					if ("url".equals(name)) {
						forecast.setUrl(readText(xpp));
					} else if ("city".equals(name)) {
						forecast.setCity(readCity(xpp));
					} else if ("current".equals(name)) {
						forecast.setCurrentForecast(readCurrentForecast(xpp));
					} else if ("forecast".equals(name)) {
						forecast.setDayForecasts(readDays());
					} else {
						Log.i("DEBUG: ", "In skip forecast");
						skipTag(xpp);
					}
				}

				xpp.require(XmlPullParser.END_TAG, namespace, "forecast");
				
				return forecast;

		
	}
	
	// Read city info
	private City readCity(XmlPullParser xpp) throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "city");
		City city = new City();
		
		while (xpp.next() != XmlPullParser.END_TAG){
			if (xpp.getEventType() == XmlPullParser.START_TAG) Log.i("DEBUG: ", "START_TAG");
			if (xpp.getEventType() == XmlPullParser.END_TAG) Log.i("DEBUG: ", "END_TAG");
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = xpp.getName();
			if ("name".equals(name)) {
				city.setName(readText(xpp));
				Log.i("DEBUG: ", "In city name");
				Log.i("DEBUG: ", city.getName());
			} else if ("name_en".equals(name)) {
				Log.i("DEBUG: ", "In city nameEN");
				city.setNameEn(readText(xpp));				
			} else if ("region".equals(name)) {
				city.setRegion(readRegion(xpp));
			} else if ("country".equals(name)) {
				city.setCountry(readCountry(xpp));
				Log.i("DEBUG: ", city.getCountry().getCountry());
			} else {
				Log.i("DEBUG: ", "In skip city");
				skipTag(xpp);
			}
		}
		xpp.require(XmlPullParser.END_TAG, namespace, "city");
		
		return city;
	}
	
	// Read region info
	private Region readRegion(XmlPullParser xpp) throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "region");

		Region region = new Region();

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("name".equals(name)) {
				region.setRegion(readText(xpp));
			} else if ("name_en".equals(name)) {
				region.setRegionEn(readText(xpp));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "region");
		Log.i("DEBUG: ", region.getRegion());
		return region;
		
	}
	
	// Read country info
	private Country readCountry(XmlPullParser xpp) throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "country");

		Country country = new Country();

		country.setCountryId(toInt(xpp.getAttributeValue(null, "id")));

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("name".equals(name)) {
				country.setCountry(readText(xpp));
			} else if ("name_en".equals(name)) {
				country.setCountryEn(readText(xpp));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "country");

		return country;
	}
	
	private CurrentForecast readCurrentForecast(XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "current");

		CurrentForecast currentForecast = new CurrentForecast();

		currentForecast.setLastUpdated(xpp.getAttributeValue(null,
				"last_updated"));
		currentForecast.setExpires(xpp.getAttributeValue(null,
				"expires"));

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("time".equals(name)) {
				currentForecast.setTime(readText(xpp));
			} else if ("cloud".equals(name)) {
				currentForecast.setCloudId(toInt(readText(xpp)));
			} else if ("pict".equals(name)) {
				currentForecast.setPictureName(readText(xpp));
			} else if ("t".equals(name)) {
				currentForecast.setTemperature(readText(xpp));
			} else if ("t_flik".equals(name)) {
				currentForecast.setTemperatureFlik(readText(xpp));
			} else if ("p".equals(name)) {
				currentForecast.setPressure(toInt(readText(xpp)));
			} else if ("w".equals(name)) {
				currentForecast.setWind(toInt(readText(xpp)));
			} else if ("w_gust".equals(name)) {
				currentForecast.setWindGust(toInt(readText(xpp)));
			} else if ("w_rumb".equals(name)) {
				currentForecast.setWindRumb(toInt(readText(xpp)));
			} else if ("h".equals(name)) {
				currentForecast.setHumidity(toInt(readText(xpp)));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "current");

		return currentForecast;
	}
	
	// Read weather on week
	
	private List<ForecastDay> readDays() throws XmlPullParserException,
			IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "forecast");

		List<ForecastDay> days = new ArrayList<ForecastDay>();

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("day".equals(name)) {
				days.add(readDay());
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "forecast");

		return days;
	}

	private ForecastDay readDay() throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "day");

		ForecastDay dayForecast = new ForecastDay();

		dayForecast.setDate(xpp.getAttributeValue(null, "date"));
		dayForecast.setHour(toInt(xpp.getAttributeValue(null, "hour")));

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("cloud".equals(name)) {
				dayForecast.setCloudId(toInt(readText(xpp)));
			} else if ("pict".equals(name)) {
				dayForecast.setPictureName(readText(xpp));
			} else if ("ppcp".equals(name)) {
				dayForecast.setPpcp(toInt(readText(xpp)));
			} else if ("t".equals(name)) {
				readTemperature(dayForecast);
			} else if ("p".equals(name)) {
				readPressure(dayForecast);
			} else if ("wind".equals(name)) {
				readWind(dayForecast);
			} else if ("hmid".equals(name)) {
				readHumidity(dayForecast);
			} else if ("wpi".equals(name)) {
				dayForecast.setWpi(toInt(readText(xpp)));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "day");

		return dayForecast;
	}

	private void readTemperature(ForecastDay dayForecast)
			throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "t");

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("min".equals(name)) {
				dayForecast.setTemperatureMin(Integer.valueOf(readText(xpp)));
			} else if ("max".equals(name)) {
				dayForecast.setTemperatureMax(Integer.valueOf(readText(xpp)));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "t");
	}

	private void readPressure(ForecastDay dayForecast)
			throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "p");

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("min".equals(name)) {
				dayForecast.setPressureMin(toInt(readText(xpp)));
			} else if ("max".equals(name)) {
				dayForecast.setPressureMax(toInt(readText(xpp)));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "p");
	}

	private void readWind(ForecastDay dayForecast)
			throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "wind");

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("min".equals(name)) {
				dayForecast.setWindMin(toInt(readText(xpp)));
			} else if ("max".equals(name)) {
				dayForecast.setWindMax(toInt(readText(xpp)));
			} else if ("rumb".equals(name)) {
				dayForecast.setWindRumb(toInt(readText(xpp)));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "wind");
	}

	private void readHumidity(ForecastDay dayForecast)
			throws XmlPullParserException, IOException {
		xpp.require(XmlPullParser.START_TAG, namespace, "hmid");

		while (xpp.next() != XmlPullParser.END_TAG) {
			if (xpp.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = xpp.getName();
			if ("min".equals(name)) {
				dayForecast.setHumidityMin(toInt(readText(xpp)));
			} else if ("max".equals(name)) {
				dayForecast.setHumidityMax(toInt(readText(xpp)));
			} else {
				skipTag(xpp);
			}
		}

		xpp.require(XmlPullParser.END_TAG, namespace, "hmid");
	}

	
	
	
	//////////// service method
	
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
	
}
