package com.unrealedz.wstation;

import com.unrealedz.wstation.UpdateService.IUpdateServiceCallBack;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.fragments.FragmentCurrent;
import com.unrealedz.wstation.fragments.FragmentInfo;
import com.unrealedz.wstation.fragments.FragmentList;
import com.unrealedz.wstation.preferences.PrefActivity;
import com.unrealedz.wstation.utils.UtilsNet;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements IUpdateServiceCallBack{
	
	FragmentCurrent fragCurrent;
	FragmentList fragList;
	FragmentInfo fragInfo;
	FragmentTransaction fTrans;
	
	LinearLayout linearLayout;

	City city;
	CurrentForecast currentForecast;
	
	ServiceConnection sConn;
	UpdateService updateService;
	boolean bound = false; 			
	boolean isRunning = false;
	boolean orientationChanged = false;
	int screenOrienrtation = 0;
		
	String url = "http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=uk";

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);          

        fragCurrent = new FragmentCurrent();
        fragList =  new FragmentList();
        fragInfo = new FragmentInfo();             
        
       // fragCurrent.setRetainInstance(true);
       // fragList.setRetainInstance(true); 
       // fragInfo.setRetainInstance(true); 
        
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragCurrent, fragCurrent);
        fTrans.add(R.id.fragList, fragList);
        fTrans.add(R.id.fragInfoUpdate, fragInfo);
        fTrans.commit();
                
        sConn = new ServiceConnection() {
        	
        	@Override
			public void onServiceConnected(ComponentName component, IBinder binder) {
		        Log.i("DEBUG", "MainActivity onServiceConnected");
		        updateService = ((UpdateService.ForecastBinder) binder).getService(); 
		        updateService.setOnUpdateServiceCallBack(MainActivity.this);
		        bound = true;		
		        //if (isRunning) {
		        if (screenOrienrtation == 0){			//loading data if the orientation has not been changed
		        	updateService.setLocationInfo();
		        	updateService.setLastUpdate();
		        	updateService.setWeekList();
		        	updateService.cityLoad();
		        	fragInfo.setProgressBar(true);    	//show progress while loading data
		        } else {								//loading data if the orientation has been changed
		        	updateService.setLocationInfo();
		        	updateService.setLastUpdate();
		        	updateService.setWeekList();
		        }
		        //}
			}
		      		    
		    public void onServiceDisconnected(ComponentName name) {
		        //Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
		        bound = false;
		    }			
		 };		 
		 bindToService();	               
    }
    
    public void bindToService() {
    	Intent intentService = new Intent(getApplicationContext(), UpdateService.class);
    	
    	//check if service is running (no for IntentServise)
        if (UtilsNet.IsServiceRunning(this)) {
            // Bind to LocalService
        	Log.i("DEBUG:", "Service is running");
            bindService(intentService, sConn, getApplicationContext().BIND_AUTO_CREATE);
            bound = true;
            isRunning = true;
            if (updateService != null)
            updateService.cityLoad();          
        }
        else {
        	Log.i("DEBUG:", "Service is not running");
            startService(intentService);
            bindService(intentService, sConn, getApplicationContext().BIND_AUTO_CREATE);
            bound = true;
        }
 
    }   	
    
	protected void onResume() {
	    super.onResume();
	}

	//CallBack: set data to fragments (Current forecast and the city location info)
	@Override
	public void onLocationCurrentPrepared(City city,
			CurrentForecast currentForecast) {
		
		fragCurrent.setData(city, currentForecast); //Current forecast
		fragList.setCity(city);						//send the city location info to ListView (for open activity_detail_day)
		
	}
	
	//CallBack: set data to fragment (last updated time)
	@Override
	public void onLastUpdatePrepared(CurrentForecast currentForecast) {

		if (currentForecast != null) fragInfo.setData(currentForecast);
		
	}
	
	
	//CallBack: set data to fragment (a forecast on 5 day)
	@Override
	public void onForecastPrepared(Cursor cursor) {
		if (cursor.getCount() != 0){
			fragList.setCursor(cursor);
		}
		fragInfo.setProgressBar(false); //hide progress while loading data
	}	
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        //call preference activity
        case R.id.action_settings:
        	Intent intentPref = new Intent(this, PrefActivity.class);
        	startActivity(intentPref);
            return true;       
        default:

        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    screenOrienrtation = getResources().getConfiguration().orientation; //save new screen orientation
	    state.putInt("screenOrienrtation", screenOrienrtation);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState != null)
	    screenOrienrtation = savedInstanceState.getInt("screenOrienrtation"); //get screen orientation
	}

	@Override
	  protected void onStop() {
	    super.onStop();
	    if (!bound) return;
	    unbindService(sConn);		//Disconnect from the service
	    bound = false;
	  }
	
	@Override
	public void onBackPressed() {
		this.finish();
	}
	

    
}
