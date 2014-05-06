package com.unrealedz.wstation.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.*;

import com.unrealedz.wstation.LocationLoader;
import com.unrealedz.wstation.MainActivity;
import com.unrealedz.wstation.NetworkLoader;
import com.unrealedz.wstation.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.R;
import com.unrealedz.wstation.R.id;
import com.unrealedz.wstation.R.layout;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.fragments.FragmentDayHours.HoursCallBack;
import com.unrealedz.wstation.utils.Utils;
import com.unrealedz.wstation.utils.UtilsDB;

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
	
	NetworkLoader nLoader;
	Cursor cursorCity;
	Cursor cursorCurrent;
	String cityname = "";
	
	LocationLoader locationLoader;
	LoaderCallBack loaderCallBack;
	
	Context context;
	
	City mCity;
	CurrentForecast currentForecast;
			
	
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
		context = container.getContext();
	    return v;
	  }
	
	

	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	   /* DataHelper dh = new DataHelper(this.getActivity());
	    DataDayHelper dd = new DataDayHelper(this.getActivity());
	    cursorCity = dh.getCursor(DbHelper.CITY_TABLE);
		cursorCurrent = dd.getCursor(DbHelper.CURRENT_DAY_TABLE);*/
		
		//loaderCallBack.setLocationInfo();		

	  }
	
	@Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	        try {
	        	loaderCallBack = (LoaderCallBack) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement LoaderCallBack");
	        }
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
		
		city.setText(mCity.getName());
		region.setText(mCity.getRegion().getRegion());
		
		temperature.setText(currentForecast.getTemperature() + "�");
		temperatureFlik.setText(getString(R.string.temperatureFlik) + currentForecast.getTemperatureFlik() + "�");
		cloud.setText(Utils.getCloud(currentForecast.getCloudId(), context));
		humidity.setText(getString(R.string.humidity) + " " + currentForecast.getHumidity() + " %");
		pressure.setText(getString(R.string.pressure) + " " + currentForecast.getPressure() + " " + getString(R.string.pressureUnit));
		wind.setText(getString(R.string.wind) + " " +  Utils.getWindOrient(currentForecast.getWindRumb(), context) + " " + currentForecast.getWind() + " " + getString(R.string.windUnit));
		String pictureName =  currentForecast.getPictureName();
		imageView.setImageResource(Utils.getBigImageId(pictureName, context));
	}
	
	
	public void onDestroy() {
	    super.onDestroy();
	    //cursorCity.close();
	    //cursorCurrent.close();
	  }

}
