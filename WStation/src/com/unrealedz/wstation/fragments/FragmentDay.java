package com.unrealedz.wstation.fragments;


import com.unrealedz.wstation.R;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentDay extends Fragment {
	
	TextView city;
	TextView region;
	TextView selectedHour;
	TextView temperatureMin;
	TextView temperatureMax;
	TextView cloud;
	TextView humidity;
	TextView humidityTitle;
	TextView pressure;	
	TextView pressureTitle;
	TextView wind;
	TextView windTitle;
	TextView tvDate;
	ImageView imageView;
	Context context;
		
	int hours;
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
	String date;
	
	SharedPreferences preferences;
	Boolean fahrenheit;
	String windUnitSpeed;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_day, container, false);
		
		city = (TextView) v.findViewById(R.id.tvCityDay);
		region = (TextView) v.findViewById(R.id.tvRegionDay);
		selectedHour = (TextView) v.findViewById(R.id.tvSelectedHour);
		tvDate = (TextView) v.findViewById(R.id.tvDetailDayDate);
		temperatureMin = (TextView) v.findViewById(R.id.tvDayTmin);
		temperatureMax = (TextView) v.findViewById(R.id.tvDayTmax);
		cloud = (TextView) v.findViewById(R.id.tvCloudHour);
		humidity = (TextView) v.findViewById(R.id.tvHumidityHour);
		humidityTitle = (TextView) v.findViewById(R.id.tvHumidityHourTitle);
		humidityTitle.setVisibility(ImageView.INVISIBLE);
		pressure = (TextView) v.findViewById(R.id.tvPressureHour);
		pressureTitle = (TextView) v.findViewById(R.id.tvPressureHourTitle);
		pressureTitle.setVisibility(ImageView.INVISIBLE);
		wind = (TextView) v.findViewById(R.id.tvWindHour);
		windTitle = (TextView) v.findViewById(R.id.tvWindHourTitle);
		windTitle.setVisibility(ImageView.INVISIBLE);
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
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    loadPreferences(activity);	
	}	
	
	//loading preferences
	private void loadPreferences(Activity activity) {
		preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		fahrenheit = preferences.getBoolean("units_temp", false);
		windUnitSpeed = preferences.getString("units_wind", getString(R.string.windUnit));
	}

	public void setData(ForecastDay forecastDay) {
		hours = forecastDay.getHour();
		date = Utils.getStringDate(forecastDay.getDate());
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
			
			
			//check if preference units do not equals to the default values(metric units)
			if (fahrenheit) {
				tmin = Utils.getFahrenheit(tmin);
				tmax =  Utils.getFahrenheit(tmax);
			}
			if (!windUnitSpeed.equals(getString(R.string.windUnit))) {
				windMin = Utils.getwindUnitSpeed(windMin, windUnitSpeed);
				windMax = Utils.getwindUnitSpeed(windMax, windUnitSpeed);
			}
			
			city.setText(cityName);
			region.setText(mRegion);
			selectedHour.setText(String.valueOf(hours) + ":00");
			tvDate.setText(date);
			temperatureMin.setText(String.valueOf(tmin) + "°");
			temperatureMax.setText(String.valueOf(tmax) + "°");
			cloud.setText(Utils.getCloud(cloudID, context));
			humidity.setText("  " + String.valueOf(humidityMin) + "/"
					+ String.valueOf(humidityMax) + " %");
			pressure.setText("  " + String.valueOf(pressureMin) + "/"
					+ String.valueOf(pressureMax) + " "
					+ getString(R.string.pressureUnit));
			wind.setText("  " + Utils.getWindOrient(windRumb, context) + " "
					+ String.valueOf(windMin) + "/" + String.valueOf(windMax)
					+ " " + windUnitSpeed);
			imageView.setImageResource(Utils
					.getBigestImageId(pictureName, context));
			imageView.setVisibility(ImageView.VISIBLE);
			
			humidityTitle.setVisibility(ImageView.VISIBLE);
			pressureTitle.setVisibility(ImageView.VISIBLE);
			windTitle.setVisibility(ImageView.VISIBLE);
		}
	}
}
