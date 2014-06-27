package com.unrealedz.wstation.fragments;



import java.util.List;

import com.unrealedz.wstation.bd.DaoWeek;
import com.unrealedz.wstation.charts.ChartView;
import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.utils.ChartDataBuilder;
import com.unrealedz.wstation.utils.Contract;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 *
 */

public class FragmentPressure extends Fragment {

	List<Point> nodesMax;
	List<Point> nodesMin;
	private int titleId;
	private ForecastDay forecastDay;
	private List<ForecastDay> forecastDays;
	private String date;
	
	public FragmentPressure(){   
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_pressure, container, false);
    	date = getArguments().getString("date");
    	getForecastDay();
       return new ChartView(getActivity(), nodesMax, nodesMin, Contract.PRESSURE);
    }

    public void setNodes(List<Point> nodes, int titleId){
    	this.nodesMax = nodesMax;
    	this.titleId = titleId;
    }
    
    private void getForecastDay() {
    	
    	DaoWeek dataWeekHelper = new DaoWeek(getActivity());		
    	forecastDays = dataWeekHelper.getForecastDayHours(date);//Get current day with hours forecast
    	ChartDataBuilder chartDataBuilder = new ChartDataBuilder();
    	nodesMax = chartDataBuilder.getPressureNodesMax(forecastDays);
    	nodesMin = chartDataBuilder.getPressureNodesMin(forecastDays);
	}

}
