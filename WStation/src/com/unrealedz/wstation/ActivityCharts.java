package com.unrealedz.wstation;

import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.unrealedz.wstation.bd.DaoWeek;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.fragments.FragmentHumidity;
import com.unrealedz.wstation.fragments.FragmentPressure;
import com.unrealedz.wstation.fragments.FragmentTemperature;
import com.unrealedz.wstation.utils.ChartDataBuilder;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.utils.Utils;

public class ActivityCharts extends Activity {

    private FragmentTemperature fragmentTemperature;
    private FragmentPressure fragmentPressure;
    private FragmentHumidity fragmentHumidity;
    private FragmentTransaction fTrans;
    TextView tvTemperatureLb;
    TextView tvPressureLb;
    TextView tvHumidityLb;
    
    private String date;
    
    private SharedPreferences preferences;
    private Boolean fahrenheit;
    
    String tempUnitValue;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        
        tvTemperatureLb = (TextView) findViewById(R.id.tvChartTemperatureLb);
        tvPressureLb = (TextView) findViewById(R.id.tvChartPressureLb);
        tvHumidityLb = (TextView) findViewById(R.id.tvChartHumidityLb);
        
        if (savedInstanceState == null ) {
        	date = getIntent().getExtras().getString("date");
        	refresh();
        }      
    }
    
    private void refresh(){
        fragmentTemperature = new FragmentTemperature();
        fragmentPressure = new FragmentPressure();
        fragmentHumidity = new FragmentHumidity();
        
        Bundle bundle = new Bundle();
        bundle.putString("date", date );
        
        fragmentTemperature.setArguments(bundle);
        fragmentPressure.setArguments(bundle);
        fragmentHumidity.setArguments(bundle);
              
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragTemp, fragmentTemperature);
        fTrans.add(R.id.fragPressure, fragmentPressure);
        fTrans.add(R.id.fragHumidity, fragmentHumidity);
        fTrans.commit();
        
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadPreferences();
        
        String temperatureText = getString(R.string.temperature_chart) + " (" + tempUnitValue + "°)";
        String pressureText = getString(R.string.pressure_chart) + " (" + getString(R.string.pressureUnit) + ")";
        String humidity = getString(R.string.humidity_chart) +  " (%)";
        tvTemperatureLb.setText(temperatureText);
        tvPressureLb.setText(pressureText);
        tvHumidityLb.setText(humidity);
    }
    
  //loading preferences
  	private void loadPreferences() {
  		preferences = PreferenceManager.getDefaultSharedPreferences(this);
  		fahrenheit = preferences.getBoolean("units_temp", false);
  		if(!fahrenheit) tempUnitValue = "C";
  		else tempUnitValue = "F";
  		
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
    
    @Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    state.putString("date", date);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState != null)
	    date = savedInstanceState.getString("date"); //get date
	    refresh();
	}
    
}
