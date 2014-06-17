package com.unrealedz.wstation;

import java.util.List;

import com.unrealedz.wstation.bd.DaoWeek;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.fragments.FragmentDay;
import com.unrealedz.wstation.fragments.FragmentDayHours;
import com.unrealedz.wstation.fragments.FragmentDayHours.HoursCallBack;
import com.unrealedz.wstation.utils.UtilsDB;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class DetailDayActivity extends Activity implements HoursCallBack{
	
	FragmentDay fragDay;
	FragmentDayHours fragDayHours;
	FragmentTransaction fTrans;
	ForecastDay forecastDay;
	private List<ForecastDay> forecastDays;
	private int hour = 0;
	private String cityName;
	private String region;
	private String date;
	
	private int screenOrienrtation = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("DEBUG:", "Day activity onCreate: ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_day);
		
		
		fragDay = new FragmentDay();
		fragDayHours = new FragmentDayHours();
		
		fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragDay, fragDay);
        fTrans.add(R.id.fragDayHours, fragDayHours);
        fTrans.commit();
        
        if (screenOrienrtation == 0){						    //loading data if the orientation has not been changed
        	date = getIntent().getExtras().getString("date");
        	cityName = getIntent().getExtras().getString("city");        
        	region = getIntent().getExtras().getString("region");
        	refresh();
        }		
	}
	
	private void refresh(){
		DaoWeek dataWeekHelper = new DaoWeek(this);
    		
		forecastDays = dataWeekHelper.getForecastDayHours(date);//Get current day with hours forecast
        fragDay.setData(forecastDays.get(hour));				//Set fragment info of 15 hours;
        fragDay.setCity(cityName, region);						//Set fragment location(city,region) info
        fragDayHours.setData(forecastDays);						//Set fragment list of hours forecast
        dataWeekHelper.closeDb();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail_day, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {	
			case R.id.charts_view:
			Intent intentCharts = new Intent(this, ActivityCharts.class);
			intentCharts.putExtra("date", date);
			startActivity(intentCharts);
	        return true;
			default:
        }
		return super.onOptionsItemSelected(item);
	}
	
	//Send to fragment selected hours forecast
	@Override
	public void setHourInfo(int hour) {
		fragDay.setData(forecastDays.get(hour));
		fragDay.refresh();
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    screenOrienrtation = getResources().getConfiguration().orientation; //save new screen orientation
	    state.putString("date", date);
	    state.putString("cityName", cityName);
	    state.putString("region", region);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		//get screen orientation
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState != null)
	    	date = savedInstanceState.getString("date");
	    	cityName = savedInstanceState.getString("cityName");
	    	region = savedInstanceState.getString("region");
	    	refresh();
	}

}
