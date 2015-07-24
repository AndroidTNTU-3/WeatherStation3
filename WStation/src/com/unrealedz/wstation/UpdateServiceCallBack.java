package com.unrealedz.wstation;

import com.unrealedz.wstation.entity.City;
import com.unrealedz.wstation.entity.CurrentForecast;

public interface UpdateServiceCallBack {
	//public void onForecastPrepared(Cursor cursor);
	public void onForecastPrepared();
	public void onLocationCurrentPrepared(City city, CurrentForecast currentForecast);
	public void onLastUpdatePrepared(CurrentForecast currentForecast);
	public void hideProgress();
}
