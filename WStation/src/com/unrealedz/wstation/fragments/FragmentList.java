package com.unrealedz.wstation.fragments;

import java.util.List;

import com.unrealedz.wstation.DetailDayActivity;
import com.unrealedz.wstation.ListWeekAdapter;
import com.unrealedz.wstation.MyCursorAdapter;
import com.unrealedz.wstation.R;
import com.unrealedz.wstation.R.id;
import com.unrealedz.wstation.R.layout;
import com.unrealedz.wstation.bd.DaoCityCurrent;
import com.unrealedz.wstation.bd.DaoWeek;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FragmentList extends Fragment{
	
	ListView listView;
	LoaderCallBack loaderCallBack;
	
	private Cursor cursor;
	private Context context;
	private String[] fromFieldNames;
	private int[] toViewIDs;
	private List<ForecastDayShort> forecastDaysShort;
	DaoWeek dataWeekHelper;
	
	private BaseAdapter baseAdapter;
	
	private String cityName;
	private String region;
	
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
            
            cursor.moveToPosition(position);
            date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE));
                        
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

	public void setDataToList() {
		
		dataWeekHelper = new DaoWeek(context);
		cursor = dataWeekHelper.getTemperatureDay(DbHelper.WEEK_TABLE);
		//dataWeekHelper.closeCursorTemperatureDay();
		if (cursor.getCount() !=0){
		MyCursorAdapter adapter = new  MyCursorAdapter(
				context,					// Context
				R.layout.rowlayout,	// Row layout template
				cursor,					// cursor (set of DB records to map)
				fromFieldNames,			// DB Column names
				toViewIDs,				// View IDs to put information in
				0);	
		
		
        listView.setAdapter(adapter);
        
       // baseAdapter = new ListWeekAdapter(forecastDaysShort, context);
       // listView.setAdapter(baseAdapter);
        adapter.swapCursor(cursor);

		}
	}

}
