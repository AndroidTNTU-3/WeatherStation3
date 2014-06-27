package com.unrealedz.wstation.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.entity.PolySector;

public class ChartDataBuilder {
		
	private int maxValue;
	private int minValue;
	private int heightAxis;
	private float multiplier;
	private int offset;
	
	List<Point> points;
	
	public List<Point> getTemperatureNodesMax(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getTemperatureMax()));
		}		
		return nodes;
	}
	
	public List<Point> getTemperatureNodesMin(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getTemperatureMin()));			
		}
		return nodes;
	}
	
	public List<Point> getPressureNodesMax(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getPressureMax()));
		}
		return nodes;
	}
	
	public List<Point> getPressureNodesMin(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getPressureMin()));
		}
		return nodes;
	}
	
	public List<Point> getHumidityNodesMax(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getHumidityMax()));
		}
		return nodes;
	}
	
	public List<Point> getHumidityNodesMin(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getHumidityMin()));
		}
		return nodes;
	}
	
	private void getMinMaxValue(int height, List<Point> nodes){ 
		List<Point> sortedPoints = new ArrayList<Point>(nodes);
		Collections.sort(sortedPoints, new ValueSort());
		
		heightAxis = height - Contract.PADDING_TOP - Contract.PADDING_BOTTOM;
		maxValue = sortedPoints.get(sortedPoints.size() - 1).y;
		minValue = sortedPoints.get(0).y;		
	}
	
	private float getMultiplier(){
		return (float) (((float)heightAxis/(maxValue-minValue))*Contract.MULT_Y_NARROW);
	}
	
	private int getOffset(){
		return (int) Math.round(((float)heightAxis - ((float)(maxValue - minValue)*multiplier))/2);
	}
	
	public List<PolySector> getChartPoint(int height, int width,
			List<Point> nodes) {
		
		getMinMaxValue(height, nodes);
		points = new ArrayList<Point>();
		List<PolySector> newPoints = new ArrayList<PolySector>();  //polygons list
		
		if(newPoints.size() != 0) newPoints.clear();	

		multiplier = getMultiplier();
		offset = getOffset();
		
		width -= Contract.PADDING_LEFT_RIGHT;
		height =  height - Contract.PADDING_TOP - Contract.PADDING_BOTTOM ;
		
		float deltaHour = width/(nodes.size()-1);
			
		int i = 0;
		for (Point p: nodes){
			Point point = new Point();		
			point.x = Math.round(deltaHour*i);
			point.y = (int) (Math.round(multiplier*(p.y-minValue)) + offset);
			points.add(point);
			i++;
		}	
		
		//set polygons
			for (int a = 0; a < points.size() - 1 ; a++) {

				PolySector ps = new PolySector();
				for (int j = 0; j < 4; j++) {
					
						switch (j) {
						case 0:
							int newX = points.get(a).x;
							int newY = height;
							ps.addToArray(new Point(newX, newY));
						break;
						case 1:
							newX = points.get(a).x;
							newY = height - points.get(a).y;
							ps.addToArray(new Point(newX, newY));
						break;
						case 2:
							newX = points.get(a + 1).x;
							newY = height - points.get(a + 1).y;
							ps.addToArray(new Point(newX, newY));
						break;
						case 3:
							newX = points.get(a + 1).x;
							newY = height;
							ps.addToArray(new Point(newX, newY));
						break;
						default:
						break;
					}
				}
				newPoints.add(ps);
				
			}

			return newPoints;
	}
	
	public List<Integer> getDivision(int height, int width,
			List<Point> nodes, int amountYParameter) {
		List<Integer> divisionPoints = new ArrayList<Integer>();
		
        float deltaT = (float) heightAxis/amountYParameter;
    		
    	for (int i = 0; i < amountYParameter + 1 ; i++){
    		int markerYPos = Math.round(deltaT*i) + Contract.PADDING_TOP;	
    		divisionPoints.add(markerYPos);
    	}
		return divisionPoints;
		
	}
	
	public List<Float> getDivisionValue(int amountYParameter){
		List<Float> paramValues = new ArrayList<Float>();
		
		int minAxisValue = (int) (minValue - Math.round(offset/multiplier));
        int maxAxisValue = (int) (maxValue + Math.round(offset/multiplier));
        
        float deltaValue = (float)(maxAxisValue - minAxisValue)/amountYParameter;
    	float currentValue = maxAxisValue;
    	    	
    	for(int i = 0; i < amountYParameter + 1; i++){
    		paramValues.add(currentValue);
    		currentValue = currentValue - deltaValue;
    	}
    	
		return paramValues; 
	}
	
	public List<Point> getPoints(){
		return points;
	}


}
