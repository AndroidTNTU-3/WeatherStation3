

package com.unrealedz.wstation.fragments;

import com.unrealedz.wstation.MainActivity;
import com.unrealedz.wstation.R;
import com.unrealedz.wstation.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.R.id;
import com.unrealedz.wstation.R.layout;
import com.unrealedz.wstation.bd.DataDayHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.CurrentForecast;
import com.unrealedz.wstation.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentInfo extends Fragment {
	
	TextView tvLastUpdate;
	Cursor cursor;
	CurrentForecast currentForecast;
	LoaderCallBack loaderCallBack;
	View view;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_info, container, false);
		tvLastUpdate = (TextView) view.findViewById(R.id.tvLastUpdate);
		return view;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	
	}
	
	@Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
	     /*   try {
	        	loaderCallBack = (LoaderCallBack) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement LoaderCallBack");
	        }*/
	  }
	
	public void setData(CurrentForecast currentForecast) {
		this.currentForecast = currentForecast;	
		refresh();
	}
	
	private void refresh(){
		String lastUpdate = getString(R.string.last_update)  + ": " + Utils.getStringLastUpDate(currentForecast.getLastUpdated());
		tvLastUpdate.setText(lastUpdate);
	}
	
	public void onDestroy() {
	    super.onDestroy();
		//cursor.close();;
	  }
	
	public void setProgressBar(boolean visible){
		ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.mainProgressBar);
		if(visible) progressBar.setVisibility(View.VISIBLE);
		else if(!visible) progressBar.setVisibility(View.INVISIBLE);
	}

}
