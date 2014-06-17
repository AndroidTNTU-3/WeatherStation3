package com.unrealedz.wstation.fragments;



import java.util.List;

import com.unrealedz.wstation.charts.ChartView;
import com.unrealedz.wstation.R;

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
public class FragmentHumidity extends Fragment {

	List<Point> nodes;
    public FragmentHumidity() {    
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_humidity, container, false);
        return new ChartView(getActivity(), nodes);
    }

    public void setNodes(List<Point> nodes){
    	this.nodes = nodes;
    }
}
