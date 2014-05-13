package com.unrealedz.wstation;

import java.util.ArrayList;
import java.util.List;

import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.fragments.FragmentDay;
import com.unrealedz.wstation.fragments.FragmentDayHours;
import com.unrealedz.wstation.fragments.FragmentDayHours.HoursCallBack;
import com.unrealedz.wstation.utils.UtilsDB;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class DetailDayActivity extends Activity implements HoursCallBack{
	
	FragmentDay fragDay;
	FragmentDayHours fragDayHours;
	FragmentTransaction fTrans;
	ForecastDay forecastDay;
	private List<ForecastDay> forecastDays;
	int hour = 0;
	String cityName;
	String region;
	String date;
	
	int screenOrienrtation = 0;

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
        
        if (screenOrienrtation == 0){
        	date = getIntent().getExtras().getString("date");
        	cityName = getIntent().getExtras().getString("city");        
        	region = getIntent().getExtras().getString("region");
        	refresh();
        }		
	}
	
	private void refresh(){
		DataWeekHelper dataWeekHelper = new DataWeekHelper(this);
		Cursor cursor = dataWeekHelper.getCursorDay(date);
		Log.i("DEBUG:", "cursor: " + cursor.getCount());
		forecastDays = UtilsDB.getForecastList(cursor);

        fragDay.setData(forecastDays.get(hour));
        fragDay.setCity(cityName, region);
        fragDayHours.setData(forecastDays);
        cursor.close();
        dataWeekHelper.closeDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail_day, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public String getDate(){
		return date;
	}

	@Override
	public void setHourInfo(int hour) {
		fragDay.setData(forecastDays.get(hour));
		fragDay.refresh();
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    screenOrienrtation = getResources().getConfiguration().orientation;
	    state.putString("date", date);
	    state.putString("cityName", cityName);
	    state.putString("region", region);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState != null)
	    	date = savedInstanceState.getString("date");
	    	cityName = savedInstanceState.getString("cityName");
	    	region = savedInstanceState.getString("region");
	    	refresh();
	}

}
