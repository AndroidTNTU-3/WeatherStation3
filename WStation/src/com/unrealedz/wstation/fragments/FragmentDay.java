package com.unrealedz.wstation.fragments;

import java.util.ArrayList;
import java.util.List;

import com.unrealedz.wstation.R;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentDay extends Fragment {
	
	TextView city;
	TextView region;
	TextView temperatureMin;
	TextView temperatureMax;
	TextView humidity;
	TextView pressure;
	TextView wind;
	ImageView imageView;
	Context context;
		
	int tmin;
	int tmax;
	int id_image;
	int humidityMin;
	int humidityMax;
	int pressureMin;
	int pressureMax;
	int windMin;
	int windMax;
	String pictureName;
	String cityName;
	String mRegion;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_day, container, false);
		
		city = (TextView) v.findViewById(R.id.tvCityDay);
		region = (TextView) v.findViewById(R.id.tvRegionDay);
		temperatureMin = (TextView) v.findViewById(R.id.tvDayTmin);
		temperatureMax = (TextView) v.findViewById(R.id.tvDayTmax);
		humidity = (TextView) v.findViewById(R.id.tvHumidityHour);
		pressure = (TextView) v.findViewById(R.id.tvPressureHour);
		wind = (TextView) v.findViewById(R.id.tvWindHour);
		imageView = (ImageView) v.findViewById(R.id.imageDay);
		context = container.getContext();
		
		city.setText(cityName);
		region.setText(mRegion);
		
		refresh();
	    return v;
	  }
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);{
	    }
	}
	
	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);
	    
	}	

	public void setData(ForecastDay forecastDay) {
		
		tmin = forecastDay.getTemperatureMin();
		tmax = forecastDay.getTemperatureMax();
		humidityMin = forecastDay.getHumidityMin();
		humidityMin = forecastDay.getHumidityMax();
		pressureMin = forecastDay.getHumidityMin();
		pressureMin = forecastDay.getHumidityMax();
		windMin = forecastDay.getWindMin();
		windMax = forecastDay.getWindMax();
		pictureName = forecastDay.getPictureName();	
	}
	
	public void setCity(String cityName, String region) {
		this.cityName = cityName;
		mRegion = region;
		
	}
	
	public void refresh(){
		temperatureMin.setText(String.valueOf(tmin) + "�");
		temperatureMax.setText(String.valueOf(tmax) + "�");
		humidity.setText("Humidity: " + String.valueOf(humidityMin) + "/" + String.valueOf(humidityMax) +  "%");
		pressure.setText("Pressure: " + String.valueOf(pressureMin) + "/" + String.valueOf(pressureMax) +  getString(R.string.pressureUnit));
		wind.setText("Wind: " + String.valueOf(windMin) + "/" + String.valueOf(windMax) + getString(R.string.windUnit));
		imageView.setImageResource(Utils.getBigImageId(pictureName, context));
	}
}
