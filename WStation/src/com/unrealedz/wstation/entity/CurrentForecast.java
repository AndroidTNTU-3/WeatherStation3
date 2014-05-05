package com.unrealedz.wstation.entity;

public class CurrentForecast {
	private String lastUpdated;
	private String expires;
	private String time;
	private int cloudId;
	private String pictureName;
	private String temperature;
	private String temperatureFlik;
	private int pressure;
	private int wind;
	private int windGust;
	private int windRumb;
	private int humidity;

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCloudId() {
		return cloudId;
	}

	public void setCloudId(int cloudId) {
		this.cloudId = cloudId;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getTemperatureFlik() {
		return temperatureFlik;
	}

	public void setTemperatureFlik(String temperatureFlik) {
		this.temperatureFlik = temperatureFlik;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getWind() {
		return wind;
	}

	public void setWind(int wind) {
		this.wind = wind;
	}

	public int getWindGust() {
		return windGust;
	}

	public void setWindGust(int windGust) {
		this.windGust = windGust;
	}

	public int getWindRumb() {
		return windRumb;
	}

	public void setWindRumb(int windRumb) {
		this.windRumb = windRumb;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "CurrentForecast [lastUpdated=" + lastUpdated + ", expires="
				+ expires + ", time=" + time + ", cloudId=" + cloudId
				+ ", pictureName=" + pictureName + ", temperature="
				+ temperature + ", temperatureFlik=" + temperatureFlik
				+ ", pressure=" + pressure + ", wind=" + wind + ", windGust="
				+ windGust + ", windRumb=" + windRumb + ", humidity="
				+ humidity + "]";
	}
}
