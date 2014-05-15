package com.unrealedz.wstation.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unrealedz.wstation.R;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.loaders.LocationLoader;
import com.unrealedz.wstation.loaders.NetworkLoader;
import com.unrealedz.wstation.loaders.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.utils.Utils;

public class FragmentCurrent extends Fragment{
	
	TextView city;
	TextView region;
	TextView temperature;
	TextView temperatureFlik;
	TextView cloud;
	TextView humidity;
	TextView pressure;
	TextView wind;
	ImageView imageView;
	
	RelativeLayout rl;
	LinearLayout linearLayout;
	
	NetworkLoader nLoader;
	Cursor cursorCity;
	Cursor cursorCurrent;
	String cityname = "";
	
	LocationLoader locationLoader;
	LoaderCallBack loaderCallBack;
	
	Context context;
	
	City mCity;
	CurrentForecast currentForecast;
	
	SharedPreferences preferences;
	Boolean fahrenheit;
	String windUnitSpeed;
	
			
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_current, container, false);
		
		city = (TextView) v.findViewById(R.id.tvCity);
		region = (TextView) v.findViewById(R.id.tvRegion);
		temperature = (TextView) v.findViewById(R.id.tvTemperature);
		temperatureFlik = (TextView) v.findViewById(R.id.tvTemperatureFlik);
		cloud = (TextView) v.findViewById(R.id.tvCloud);
		humidity = (TextView) v.findViewById(R.id.tvHumidity);
		pressure = (TextView) v.findViewById(R.id.tvPressure);
		wind = (TextView) v.findViewById(R.id.tvWind);
		imageView = (ImageView) v.findViewById(R.id.imageCurrent);
		imageView.setVisibility(ImageView.INVISIBLE);
		linearLayout = (LinearLayout) v.findViewById(R.id.rect_gray1);
		linearLayout.setVisibility(ImageView.INVISIBLE);
		context = container.getContext();
	    return v;
	  }
	
	

	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	  }
	
	@Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    loadPreferences(activity);		
	        /*try {
	        	loaderCallBack = (LoaderCallBack) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement LoaderCallBack");
	        }*/
	    
	  }
	
	//loading preferences
	private void loadPreferences(Activity activity) {
		preferences = PreferenceManager.getDefaultSharedPreferences(activity);
		fahrenheit = preferences.getBoolean("units_temp", false);
		windUnitSpeed = preferences.getString("units_wind", getString(R.string.windUnit));
	}



	public void onStart() {
	    super.onStart();
	  }

	public void setData(City mCity, CurrentForecast currentForecast) {
		this.mCity = mCity;
		this.currentForecast = currentForecast;
		refresh();
		
	}
	
	public void refresh() {
		String temp = currentForecast.getTemperature(); 
		String tempFlik = currentForecast.getTemperatureFlik(); 
		int windSpeed = currentForecast.getWind();

		//check if preference units do not equals to the default values(metric units)
		if (fahrenheit) {
			temp = Utils.getFahrenheit(temp);
			tempFlik =  Utils.getFahrenheit(tempFlik);
		}
		if (!windUnitSpeed.equals(getString(R.string.windUnit))) windSpeed = Utils.getwindUnitSpeed(windSpeed, windUnitSpeed);

		city.setText(mCity.getName());
		region.setText(mCity.getRegion().getRegion());
		temperature.setText(temp + "°");
		temperatureFlik.setText(getString(R.string.temperatureFlik) + tempFlik
				+ "°");
		cloud.setText(Utils.getCloud(currentForecast.getCloudId(), context));
		humidity.setText(getString(R.string.humidity) + " "
				+ currentForecast.getHumidity() + " %");
		pressure.setText(getString(R.string.pressure) + " "
				+ currentForecast.getPressure() + " "
				+ getString(R.string.pressureUnit));
		wind.setText(getString(R.string.wind) + " "
				+ Utils.getWindOrient(currentForecast.getWindRumb(), context)
				+ " " + windSpeed + " " + windUnitSpeed);
		String pictureName = currentForecast.getPictureName();
		imageView.setVisibility(ImageView.VISIBLE);
		imageView.setImageResource(Utils.getBigImageId(pictureName, context));
		linearLayout.setVisibility(ImageView.VISIBLE);
	}
	
	
	public void onDestroy() {
	    super.onDestroy();
	  }

}
