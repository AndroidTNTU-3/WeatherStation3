package com.unrealedz.wstation.fragments;



import java.util.List;

import com.unrealedz.wstation.charts.ChartView;

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
public class FragmentTemperature extends Fragment {

	List<Point> nodes;
	private int titleId;
	
    public FragmentTemperature() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_temperature, container, false);
        return new ChartView(getActivity(), nodes, titleId);
    }
    
    public void setNodes(List<Point> nodes, int titleId){
    	this.nodes = nodes;
    	this.titleId = titleId;
    }


}
