package com.unrealedz.wstation.entity;

import java.util.List;

public class CitiesDB {
	
	private String lastUpdated;
	private String version;
	private List<CityDB> cityDB;
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getVersion() {
		return version;
	}
	public List<CityDB> getCityDB() {
		return cityDB;
	}
	public void setCityDB(List<CityDB> cityDB) {
		this.cityDB = cityDB;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
