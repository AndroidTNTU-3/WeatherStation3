package com.unrealedz.wstation.fragments;



import java.util.List;

import com.unrealedz.wstation.bd.DaoWeek;
import com.unrealedz.wstation.charts.ChartView;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.utils.ChartDataBuilder;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.R;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class FragmentHumidity extends Fragment {

	List<Point> nodes;
	private int titleId;
	private ForecastDay forecastDay;
	private List<ForecastDay> forecastDays;
	private String date;
    public FragmentHumidity() {    
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_humidity, container, false);
    	Log.i("DEBUG Frag:", "On CreateView");
    	date = getArguments().getString("date");
    	getForecastDay();
        return new ChartView(getActivity(), nodes, Contract.HUMIDITY);
    }

    public void setNodes(List<Point> nodes, int titleId){
    	Log.i("DEBUG Frag:", "Set node");
    	this.nodes = nodes;
    	this.titleId = titleId;
    }
    
    private void getForecastDay() {
    	
    	DaoWeek dataWeekHelper = new DaoWeek(getActivity());		
    	forecastDays = dataWeekHelper.getForecastDayHours(date);//Get current day with hours forecast
    	nodes = ChartDataBuilder.getHumidityNodes(forecastDays);
	}
}
