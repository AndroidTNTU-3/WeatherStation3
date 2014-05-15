package com.unrealedz.wstation.entity;


//////////////////////////////////////////////////////////
//Child class for listView on (FragmentList) MainActivity/
//contains only required filed for listView				//
//////////////////////////////////////////////////////////

public class ForecastDayShort extends ForecastDay{

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
