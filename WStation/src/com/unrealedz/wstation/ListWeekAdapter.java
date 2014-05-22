package com.unrealedz.wstation;

import java.util.List;

import com.unrealedz.wstation.entity.ForecastDayShort;
import com.unrealedz.wstation.utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListWeekAdapter extends BaseAdapter {
	
	List<ForecastDayShort> forecastDaysShort;
	Context context;
	
	SharedPreferences preferences;
	Boolean fahrenheit;
	String windUnitSpeed;
	
	public ListWeekAdapter(List<ForecastDayShort> forecastDaysShort, Context context){
		this.forecastDaysShort = forecastDaysShort;
		this.context = context;
		loadPreferences(context);
	}
	
	private static class ViewHolder{
		public TextView textViewTMin;
		public TextView textViewTMax;
		public TextView textViewDate;
		public TextView textViewCloud;
		public ImageView imageView;		
	}
	
	//loading preferences
			private void loadPreferences(Context context) {
				preferences = PreferenceManager.getDefaultSharedPreferences(context);
				fahrenheit = preferences.getBoolean("units_temp", false);
				windUnitSpeed = preferences.getString("units_wind", context.getString(R.string.windUnit));
			}

	@Override
	public int getCount() {
		
		return forecastDaysShort.size();
	}

	@Override
	public Object getItem(int position) {
		
		return forecastDaysShort.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null){
			
			convertView = LayoutInflater.from(context).inflate(
					R.layout.rowlayout, parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.textViewTMin = (TextView) convertView.findViewById(R.id.textTmin);
			viewHolder.textViewTMax = (TextView) convertView.findViewById(R.id.textTmax);
			viewHolder.textViewDate = (TextView) convertView.findViewById(R.id.textDate);
			viewHolder.textViewCloud = (TextView) convertView.findViewById(R.id.textCloud);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageList);
			
			convertView.setTag(viewHolder);
		}
		
		ForecastDayShort forecastDayShort = forecastDaysShort.get(position);
		
		
		String tMin = String.valueOf(forecastDayShort.getTemperatureMin());
		String tMax = String.valueOf(forecastDayShort.getTemperatureMax());
		String pictureName = forecastDayShort.getPictureName();
		String date = forecastDayShort.getDate();
		int cloudId = forecastDayShort.getCloudId();
		
		//check if preference units do not equals to the default values(metric units)
		if (fahrenheit) {
			tMin = Utils.getFahrenheit(tMin);
			tMax =  Utils.getFahrenheit(tMax);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		holder.textViewTMin.setText(tMin + "°");
		holder.textViewTMax.setText(tMax + "°");
		holder.textViewDate.setText(Utils.getStringDate(date));
		holder.textViewCloud.setText(Utils.getCloud(cloudId, context));
		holder.imageView.setImageResource(Utils.getImageId(pictureName, context));
		
		return convertView;
	}

}
