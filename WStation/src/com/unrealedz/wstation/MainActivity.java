package com.unrealedz.wstation;

import com.unrealedz.wstation.UpdateService.IUpdateServiceCallBack;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.fragments.FragmentCurrent;
import com.unrealedz.wstation.fragments.FragmentInfo;
import com.unrealedz.wstation.fragments.FragmentList;
import com.unrealedz.wstation.utils.UtilsNet;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

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
	boolean bound = false; 			//check if activity connected to service
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
        
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        //inflater.inflate(R.layout.activity_main, false);
     //   linearLayout = (LinearLayout) this.getLayoutInflater()findViewById(R.layout.activity_main); 
        
       // fragCurrent.setRetainInstance(true);
       // fragList.setRetainInstance(true); 
       // fragInfo.setRetainInstance(true); 
        
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.fragCurrent, fragCurrent);
        fTrans.add(R.id.fragList, fragList);
        fTrans.add(R.id.fragInfoUpdate, fragInfo);
        fTrans.commit();
     
        Log.i("DEBUG CUR", "Main On Activity Created") ;
        
       // linearLayout.setVisibility(View.INVISIBLE);
        
        sConn = new ServiceConnection() {
        	
        	@Override
			public void onServiceConnected(ComponentName component, IBinder binder) {
		        Log.i("DEBUG", "MainActivity onServiceConnected");
		        updateService = ((UpdateService.ForecastBinder) binder).getService(); 
		        updateService.setOnUpdateServiceCallBack(MainActivity.this);
		        bound = true;		
		        //if (isRunning) {
		        if (screenOrienrtation == 0){
		        	updateService.setLocationInfo();
		        	updateService.setLastUpdate();
		        	updateService.setWeekList();
		        	updateService.cityLoad();
		        	fragInfo.setProgressBar(true);
		        } else {
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

	@Override
	public void onLocationCurrentPrepared(City city,
			CurrentForecast currentForecast) {
		
		fragCurrent.setData(city, currentForecast);
		fragList.setCity(city);
		
	}
	
	public void Tested(String nameLocation){
		Toast.makeText(this, nameLocation, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onLastUpdatePrepared(CurrentForecast currentForecast) {

		if (currentForecast != null) fragInfo.setData(currentForecast);
		
	}
	
	@Override
	public void onForecastPrepared(Cursor cursor) {
		if (cursor.getCount() != 0){
			fragList.setCursor(cursor);
		}
		fragInfo.setProgressBar(false);
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
        
        case R.id.action_refresh:
        	//refresh();       	
            return true;       
        default:

        }
        return super.onOptionsItemSelected(item);
    }
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    screenOrienrtation = getResources().getConfiguration().orientation;
	    state.putInt("screenOrienrtation", screenOrienrtation);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState != null)
	    screenOrienrtation = savedInstanceState.getInt("screenOrienrtation");
	}

	@Override
	  protected void onStop() {
	    super.onStop();
	    if (!bound) return;
	    unbindService(sConn);		//Disconnect from the service
	    bound = false;
	  }

    
}
