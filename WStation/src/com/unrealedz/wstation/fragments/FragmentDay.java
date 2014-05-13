package com.unrealedz.wstation.fragments;

import java.util.ArrayList;
import java.util.List;

import com.unrealedz.wstation.R;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
	TextView cloud;
	TextView humidity;
	TextView pressure;
	TextView wind;
	ImageView imageView;
	Context context;
		
	int tmin;
	int tmax;
	int id_image;
	int cloudID;
	int humidityMin;
	int humidityMax;
	int pressureMin;
	int pressureMax;
	int windMin;
	int windMax;
	int windRumb;
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
		cloud = (TextView) v.findViewById(R.id.tvCloudHour);
		humidity = (TextView) v.findViewById(R.id.tvHumidityHour);
		pressure = (TextView) v.findViewById(R.id.tvPressureHour);
		wind = (TextView) v.findViewById(R.id.tvWindHour);
		imageView = (ImageView) v.findViewById(R.id.imageDay);
		imageView.setVisibility(ImageView.INVISIBLE);
		context = container.getContext();

	    return v;
	  }
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
		refresh();
	}
	
	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);

	}	

	public void setData(ForecastDay forecastDay) {
		tmin = forecastDay.getTemperatureMin();
		tmax = forecastDay.getTemperatureMax();
		cloudID = forecastDay.getCloudId();
		humidityMin = forecastDay.getHumidityMin();
		humidityMax = forecastDay.getHumidityMax();
		pressureMin = forecastDay.getPressureMin();
		pressureMax = forecastDay.getPressureMax();
		windMin = forecastDay.getWindMin();
		windMax = forecastDay.getWindMax();
		windRumb = forecastDay.getWindRumb();
		pictureName = forecastDay.getPictureName();	
	}
	
	public void setCity(String cityName, String region) {
		this.cityName = cityName;
		mRegion = region;

	}
	
	public void refresh() {
		if (cityName != null) {
			city.setText(cityName);
			region.setText(mRegion);
			temperatureMin.setText(String.valueOf(tmin) + "°");
			temperatureMax.setText(String.valueOf(tmax) + "°");
			cloud.setText(Utils.getCloud(cloudID, context));
			humidity.setText(getString(R.string.humidity) + " "
					+ String.valueOf(humidityMin) + "/"
					+ String.valueOf(humidityMax) + " %");
			pressure.setText(getString(R.string.pressure) + " "
					+ String.valueOf(pressureMin) + "/"
					+ String.valueOf(pressureMax) + " "
					+ getString(R.string.pressureUnit));
			wind.setText(getString(R.string.wind) + " "
					+ Utils.getWindOrient(windRumb, context) + " "
					+ String.valueOf(windMin) + "/" + String.valueOf(windMax)
					+ " " + getString(R.string.windUnit));
			imageView.setImageResource(Utils
					.getBigImageId(pictureName, context));
			imageView.setVisibility(ImageView.VISIBLE);
		}
	}
}
