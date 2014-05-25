package com.unrealedz.wstation.preferences;

import com.unrealedz.wstation.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;

public class PrefActivity extends PreferenceActivity {
	
	CheckBoxPreference chbLocationManual;
	SwitchPreference switchRefresh;
	SharedPreferences preferences;
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.pref);
	    chbLocationManual = (CheckBoxPreference) findPreference("manual_location");
	    switchRefresh = (SwitchPreference) findPreference("PromoNotificationOnOff");
	    
	    switchRefresh.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				Editor ed = preferences.edit();
 
				if(newValue.equals(true)){
					ed.putString("refreshOnOff", "1");					//if switch refresh is will enable
				    ed.commit();
				 } else {
					 ed.putString("refreshOnOff", "0");					
					 ed.commit(); ;
				 }
				return true;
			}
		});
	    		
	    chbLocationManual.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if(!chbLocationManual.isChecked()){
					preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					Editor ed = preferences.edit();
				    ed.putString("cityId", "");					//if switch get location from geoCoder
				    ed.commit();
				}
				return false;
			}
		});
	    
	  }

}
