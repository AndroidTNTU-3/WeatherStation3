package com.unrealedz.wstation.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilsNet {
	
	public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
	
	public static boolean IsServiceRunning(Context context) {
		 
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
 
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.unrealedz.wstation.UpdateService".equals(service.service.getClassName())) {
                return true;
            }
        }
 
        return false;
    }
	

}
