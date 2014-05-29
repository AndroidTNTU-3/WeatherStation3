package com.unrealedz.wstation;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;



public class ForecastWidget extends AppWidgetProvider {
	
	private PendingIntent pIntentService = null;
	private SharedPreferences preferences;
	private int refreshTime;
	private int refreshOnOff;
	
	@Override
	  public void onEnabled(Context context) {
	    super.onEnabled(context);
	    
	    ComponentName service = new ComponentName(context, UpdateService.class);	
	    loadPreferences(context);
		Intent intentStart = new Intent(UpdateService.FROM_WIDGET).setComponent(service);
		PendingIntent pIntentStart = PendingIntent.getService(context, 0, intentStart, 0);	   
		try {
			pIntentStart.send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
	
	private void loadPreferences(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		refreshTime = Integer.parseInt(preferences.getString("refreshTime", "30"));
		//refreshOnOff = Integer.parseInt(preferences.getString("refreshOnOff", "2")); // "2" value if widget first start
	}
	
	  
    @Override  
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)  
    {  
      
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
	    
	    for (int i : appWidgetIds) {
	        updateWidget(context, appWidgetManager, i);
	      }	 
    	
	    loadPreferences(context);
	    Log.i("DEBUG", "refreshtime: " + String.valueOf(refreshTime));
    	final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);   
        
        final Calendar TIME = Calendar.getInstance();  
        TIME.set(Calendar.MINUTE, 0);  
        TIME.set(Calendar.SECOND, 0);  
        TIME.set(Calendar.MILLISECOND, 0);
        
        ComponentName service = new ComponentName(context,
        		UpdateService.class);
  
        //final Intent i = new Intent(context, UpdateService.class);  
        final Intent i = new Intent(UpdateService.FROM_WIDGET).setComponent(service);  
        if (pIntentService == null)  
        {  
        	pIntentService = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);  
        }  
       // if (refreshOnOff == 1)
        m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), 1000 * 60 * refreshTime, pIntentService);
        //else if (refreshOnOff == 0) m.cancel(pIntentService);
    }
       
    
    static void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetID){
    	RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
    	Intent mainIntent = new Intent(context, MainActivity.class);
    	//mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	PendingIntent pIntentMainStart = PendingIntent.getActivity(context, 0, mainIntent, 0);
    	
    	remoteViews.setOnClickPendingIntent(R.id.widget, pIntentMainStart);
    	
    	appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }
    

    
    @Override  
    public void onDisabled(Context context)  
    {  
        final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
  
        m.cancel(pIntentService);
    }  

}
