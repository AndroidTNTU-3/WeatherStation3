package com.unrealedz.wstation.fragments;

import java.util.List;

import com.unrealedz.wstation.DetailDayActivity;
import com.unrealedz.wstation.MyCursorAdapter;
import com.unrealedz.wstation.R;
import com.unrealedz.wstation.R.id;
import com.unrealedz.wstation.R.layout;
import com.unrealedz.wstation.bd.DataHelper;
import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.ForecastDayShort;
import com.unrealedz.wstation.loaders.NetworkLoader.LoaderCallBack;
import com.unrealedz.wstation.utils.UtilsDB;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FragmentList extends Fragment{
	
	ListView listView;
	Cursor cursor;
	Context context;
	String[] fromFieldNames;
	int[] toViewIDs;
	MyCursorAdapter adapter;
	List<ForecastDayShort> ForecastDaysShort;
	LoaderCallBack loaderCallBack;
	
	String cityName;
	String region;
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		listView = (ListView) view.findViewById(R.id.listView1);
		listView.setOnItemClickListener(new ListListener());
		fromFieldNames = new String[] 
				{DbHelper.TEMPERATURE_MIN, DbHelper.TEMPERATURE_MAX};
		toViewIDs = new int[]
				{R.id.textTmin, R.id.textTmax};
		context = container.getContext();
	    return view;		
	  }
	
	@Override
    public void onAttach( Activity activity ) {
		super.onAttach(activity);
      /*  try {
        	loaderCallBack = (LoaderCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement LoaderCallBack");
        }*/
	}
	
	
	@Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
	}

	public void setCursor(Cursor c) {
		cursor = c;
		MyCursorAdapter adapter = new  MyCursorAdapter(
				context,					// Context
				R.layout.rowlayout,	// Row layout template
				cursor,					// cursor (set of DB records to map)
				fromFieldNames,			// DB Column names
				toViewIDs,				// View IDs to put information in
				0);	
        listView.setAdapter(adapter);
        ForecastDaysShort = UtilsDB.getForecastListMain(cursor);
        adapter.swapCursor(cursor);
	}
	
	public void setCity(City city){
		cityName = city.getName();
		region = city.getRegion().getRegion();
	}
	
	public class ListListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String date = ""; 			           
			Log.i("DEBUG", "ID:" + id);
            for(ForecastDayShort fd: ForecastDaysShort){
            	if (fd.getId() == id){
            		date = fd.getDate(); // get selected date
            	}
            }
            
            Intent intent = new Intent(context, DetailDayActivity.class);
            intent.putExtra("date", date);
            intent.putExtra("city", cityName);
            intent.putExtra("region", region);
            startActivity(intent);
            
		}
    	
    }
	
	public void onDestroy() {
	    super.onDestroy();
	    //((MyCursorAdapter) listView.getAdapter()).getCursor().close();
	    
	  }

}
