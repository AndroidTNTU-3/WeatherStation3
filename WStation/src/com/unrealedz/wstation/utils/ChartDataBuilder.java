package com.unrealedz.wstation.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Point;
import android.util.Log;

import com.unrealedz.wstation.entity.ForecastDay;
import com.unrealedz.wstation.entity.PolySector;

public class ChartDataBuilder {
	
	
	
	public static List<Point> getTemperatureNodesMax(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getTemperatureMax()));
		}		
		return nodes;
	}
	
	public static List<Point> getTemperatureNodesMin(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getTemperatureMin()));			
		}
		return nodes;
	}
	
	public static List<Point> getPressureNodesMax(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getPressureMax()));
		}
		return nodes;
	}
	
	public static List<Point> getPressureNodesMin(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getPressureMin()));
		}
		return nodes;
	}
	
	public static List<Point> getHumidityNodesMax(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getHumidityMax()));
		}
		return nodes;
	}
	
	public static List<Point> getHumidityNodesMin(List<ForecastDay> forecastDays) {
		List<Point> nodes = new ArrayList<Point>();
		for(ForecastDay fd: forecastDays){
			nodes.add(new Point(fd.getHour(), fd.getHumidityMin()));
		}
		return nodes;
	}

	public static List<PolySector> getChartPoint(int height, int width,
			List<Point> nodes) {
		
		//if (nodes.size() != 0){
		List<Point> points = new ArrayList<Point>();
		List<Point> sortedPoints = new ArrayList<Point>(nodes);
		List<PolySector> newPoints = new ArrayList<PolySector>();  //polygons list
		
		if(newPoints.size() != 0) newPoints.clear();
		
		Collections.sort(sortedPoints, new ValueSort());
		
		int MaxValue = sortedPoints.get(sortedPoints.size() - 1).y;
		int MinValue = sortedPoints.get(0).y;
		int heightAxis = height - Contract.PADDING_LEFT_RIGHT;

	
		float multiplier = (float) (((float)heightAxis/(MaxValue-MinValue))*Contract.MULT_Y_NARROW);	
		Log.i("DEBUG:", "multiplier = " + String.valueOf(multiplier));
		width -= Contract.PADDING_LEFT_RIGHT;
		height -= Contract.PADDING_LEFT_RIGHT;
		
		int deltaHour = width/(nodes.size()-1);
		
		int offset = (int) Math.round(((float)heightAxis - ((float)(MaxValue - MinValue)*multiplier))/2);				//offset point relative X axis to up(align to middle Y axis)
		Log.i("DEBUG:", "offset_1 = " + String.valueOf(offset));
		//offset = 0;
		//int offsetOnDegree = (int) ((heightAxis - offset*2)/multiplier);
		//int offsetOnPx = (int) (heightAxis - offsetOnDegree*multiplier);
		//if ((MinValue - offsetOnDegree) < 0)  offset = offset - offsetOnPx;
		int i = 0;
		for (Point p: nodes){
			Point point = new Point();		
			point.x = deltaHour*i;
			point.y = (int) (Math.round(multiplier*(p.y-MinValue)) + offset);
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



}
