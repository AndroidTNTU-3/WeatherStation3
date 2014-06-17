package com.unrealedz.wstation;

import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.unrealedz.wstation.bd.DaoWeek;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.fragments.FragmentHumidity;
import com.unrealedz.wstation.fragments.FragmentPressure;
import com.unrealedz.wstation.fragments.FragmentTemperature;
import com.unrealedz.wstation.utils.Utils;

public class ActivityCharts extends Activity {

    private FragmentTemperature fragmentTemperature;
    private FragmentPressure fragmentPressure;
    private FragmentHumidity fragmentHumidity;
    private FragmentTransaction fTrans;
    
    private String date;
    private ForecastDay forecastDay;
    private List<ForecastDay> forecastDays;
    private List<Point> nodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        
        date = getIntent().getExtras().getString("date");

		getForecastDay();
        fragmentTemperature = new FragmentTemperature();
        fragmentPressure = new FragmentPressure();
        fragmentHumidity = new FragmentHumidity();
        
    	nodes = Utils.getTemperatureNodes(forecastDays);
        fragmentTemperature.setNodes(nodes);
        
        
    	nodes = Utils.getPressureNodes(forecastDays);
        fragmentPressure.setNodes(nodes);
        for(Point p: nodes){
        	Log.i("DEBUG", "x: " + p.x + " y: " + p.y);
        }
        
        nodes = Utils.getHumidityNodes(forecastDays);
        fragmentHumidity.setNodes(nodes);

        
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragTemp, fragmentTemperature);
        fTrans.add(R.id.fragPressure, fragmentPressure);
        fTrans.add(R.id.fragHumidity, fragmentHumidity);
        fTrans.commit();

    }


    private void getForecastDay() {
    	
    	DaoWeek dataWeekHelper = new DaoWeek(this);		
    	forecastDays = dataWeekHelper.getForecastDayHours(date);//Get current day with hours forecast

	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_activityfr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
