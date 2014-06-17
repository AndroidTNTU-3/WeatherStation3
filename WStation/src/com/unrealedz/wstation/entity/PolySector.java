package com.unrealedz.wstation.entity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class PolySector {
	
	private Point point;
	private List<Point> innerPoint;
	
	public PolySector(){
		innerPoint = new ArrayList<Point>();
	}
	
	public void addToArray(Point p){
		innerPoint.add(p);
	}
	
	public Point getPoint(int i){
		return innerPoint.get(i);
	}
	
	public List<Point> getArray(){
		return innerPoint;
	}

}
