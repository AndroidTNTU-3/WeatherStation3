package com.unrealedz.wstation;

import com.unrealedz.wstation.bd.DataWeekHelper;
import com.unrealedz.wstation.bd.DbHelper;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class MyCursorLoader extends CursorLoader {
	
	DataWeekHelper dataWeekHelper;

	public MyCursorLoader(Context context) {
		super(context);
		dataWeekHelper = new DataWeekHelper(context);		
	}
	
	@Override
    public Cursor loadInBackground() {
		
		//Cursor cursor = dataWeekHelper.getCursor(DbHelper.WEEK_TABLE);
		Cursor cursor = dataWeekHelper.getTemperatureDay(DbHelper.WEEK_TABLE);
		return cursor;	
		
	}

}
