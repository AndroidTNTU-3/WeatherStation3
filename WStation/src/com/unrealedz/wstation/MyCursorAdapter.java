package com.unrealedz.wstation;

import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.utils.Utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends SimpleCursorAdapter {
	
	private int layout;
	private Context context;
	private LayoutInflater inflater;
	

	public MyCursorAdapter(Context _context, int _layout, Cursor cursor,
			String[] from, int[] to, int flags) {
		super(_context, _layout, cursor, from, to, flags);
		layout = _layout;
		context = _context;
		inflater = LayoutInflater.from(_context);
	}
	
	@Override
	 public void bindView(View view, Context _context, Cursor cursor) {
		super.bindView(view, _context, cursor);
		
		String tMin = cursor.getString(cursor.getColumnIndex(DbHelper.TEMPERATURE_MIN));
		String tMax = cursor.getString(cursor.getColumnIndex(DbHelper.TEMPERATURE_MAX));
		String pictureName = cursor.getString(cursor.getColumnIndex(DbHelper.PICTURE_NAME));
		String date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE));
		//String hour = cursor.getString(cursor.getColumnIndex(DbHelper.HOUR));
		
		TextView textViewTMin = (TextView) view.findViewById(R.id.textTmin);
		TextView textViewTMax = (TextView) view.findViewById(R.id.textTmax);
		TextView textViewDate = (TextView) view.findViewById(R.id.textDate);
		//TextView textViewHour = (TextView) view.findViewById(R.id.textHour);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageList);
		
		textViewTMin.setText(tMin + "∞");
		textViewTMax.setText(tMax + "∞");
		textViewDate.setText(Utils.getStringDate(date));
		//textViewHour.setText(hour + " „Ó‰");
		imageView.setImageResource(Utils.getImageId(pictureName, _context));
		
		
	 }
	
	@Override
	 public View newView(Context _context, Cursor _cursor, ViewGroup parent) {

		return inflater.inflate(layout, parent, false);

	}

	

}
