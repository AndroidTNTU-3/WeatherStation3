package com.unrealedz.wstation.entity;

import java.util.List;


public class Forecast {
	private String lastUpdated;
	private String url;
	private City city;
	private CurrentForecast currentForecast;
	private List<ForecastDay> forecastDays;

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public CurrentForecast getCurrentForecast() {
		return currentForecast;
	}

	public void setCurrentForecast(CurrentForecast currentForecast) {
		this.currentForecast = currentForecast;
	}

	public List<ForecastDay> getForecastDays() {
		return forecastDays;
	}

	public void setDayForecasts(List<ForecastDay> dayForecasts) {
		this.forecastDays = dayForecasts;
	}

	@Override
	public String toString() {
		return "Forecast [lastUpdated=" + lastUpdated + ", url=" + url
				+ ", city=" + city + ", currentForecast=" + currentForecast
				+ "]";
	}
}
