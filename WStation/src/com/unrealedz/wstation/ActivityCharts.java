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
import com.unrealedz.wstation.utils.ChartDataBuilder;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.utils.Utils;

public class ActivityCharts extends Activity {

    private FragmentTemperature fragmentTemperature;
    private FragmentPressure fragmentPressure;
    private FragmentHumidity fragmentHumidity;
    private FragmentTransaction fTrans;
    
    private String date;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        
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
