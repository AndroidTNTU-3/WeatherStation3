package com.unrealedz.wstation.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unrealedz.wstation.R;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FragmentDayHours extends Fragment {
	
	public static interface HoursCallBack{
		public void setHourInfo(int hour);
	}
	
	HoursCallBack hoursCallBack;
	
	List<ForecastDay> forecastDays;
	
	int tmin;
	int tmax;
	int id_image;
	int hour;
	
	Context context;	
	
	View v;
	LinearLayout linearLayout;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_day_hours, container, false);
		linearLayout = (LinearLayout) v.findViewById(R.id.linearLayoutHours);
		
		context = container.getContext();

		setInfo();
		
	    return v;
	  }

	private void setInfo() {
		
		LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f);
		
		for(int i = 0; i < forecastDays.size(); i++){
			LinearLayout linearl = new LinearLayout(context);
			
			linearl.setOnClickListener(new hourClickListener());
			linearl.setLayoutParams(linLayoutParam);
			linearl.setGravity(Gravity.CENTER);
			linearl.setPadding(2, 2, 2, 2);
			linearl.setOrientation(LinearLayout.VERTICAL);
			linearl.setId(i);
			
			TextView tv = new TextView(context);
			TextView tvm = new TextView(context);
			ImageView im = new ImageView(context);
			
			tv.setLayoutParams(textParams);
			tvm.setLayoutParams(textParams);
			linearl.addView(tv);
			linearl.addView(im);
			linearl.addView(tvm);
			linearLayout.addView(linearl);
							
			tv.setText(String.valueOf(forecastDays.get(i).getHour()) + ".00");
			im.setImageResource(Utils.getImageId(forecastDays.get(i).getPictureName(), context));
			String tempMinMax = forecastDays.get(i).getTemperatureMin() + "°" + "/" + forecastDays.get(i).getTemperatureMax() + "°";
			tvm.setText(tempMinMax);
			}							
		
	}

	public void setData(List<ForecastDay> forecastDays) {
		this.forecastDays = forecastDays;		
	}
	
	private class hourClickListener implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case 0: 
				hoursCallBack.setHourInfo(0);
				break;
			case 1: 
				hoursCallBack.setHourInfo(1);
				break;
			case 2: 
				hoursCallBack.setHourInfo(2);
				break;
			case 3: 
				hoursCallBack.setHourInfo(3);
				break;
			}
			
		}
	}

	@Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	        try {
	        	hoursCallBack = (HoursCallBack) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement HoursCallBack");
	        }
	  }

}
