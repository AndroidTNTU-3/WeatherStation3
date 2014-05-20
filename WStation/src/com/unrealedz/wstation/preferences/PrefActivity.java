package com.unrealedz.wstation.preferences;

import com.unrealedz.wstation.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity {
	
	CheckBoxPreference chbLocationManual;
	SharedPreferences preferences;
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.pref);
	    chbLocationManual = (CheckBoxPreference) findPreference("manual_location");
	    
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
