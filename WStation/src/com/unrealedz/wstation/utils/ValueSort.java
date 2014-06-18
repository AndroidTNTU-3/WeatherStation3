package com.unrealedz.wstation.utils;

import java.util.Comparator;

import android.graphics.Point;

public class ValueSort implements Comparator<Point>{

	@Override
	public int compare(Point one, Point two) {
		// TODO Auto-generated method stub
		return one.y - two.y;
	}

}
