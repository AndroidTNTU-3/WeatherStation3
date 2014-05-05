package com.unrealedz.wstation.entity;

public class ForecastDay {
	private String date;
	private int hour;
	private int cloudId;
	private String pictureName;
	private int ppcp;
	private int temperatureMin;
	private int temperatureMax;
	private int pressureMin;
	private int pressureMax;
	private int windMin;
	private int windMax;
	private int windRumb;
	private int humidityMin;
	private int humidityMax;
	private int wpi;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
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

	public int getPpcp() {
		return ppcp;
	}

	public void setPpcp(int ppcp) {
		this.ppcp = ppcp;
	}

	public int getTemperatureMin() {
		return temperatureMin;
	}

	public void setTemperatureMin(int temperatureMin) {
		this.temperatureMin = temperatureMin;
	}

	public int getTemperatureMax() {
		return temperatureMax;
	}

	public void setTemperatureMax(int temperatureMax) {
		this.temperatureMax = temperatureMax;
	}

	public int getPressureMin() {
		return pressureMin;
	}

	public void setPressureMin(int pressureMin) {
		this.pressureMin = pressureMin;
	}

	public int getPressureMax() {
		return pressureMax;
	}

	public void setPressureMax(int pressureMax) {
		this.pressureMax = pressureMax;
	}

	public int getWindMin() {
		return windMin;
	}

	public void setWindMin(int windMin) {
		this.windMin = windMin;
	}

	public int getWindMax() {
		return windMax;
	}

	public void setWindMax(int windMax) {
		this.windMax = windMax;
	}

	public int getWindRumb() {
		return windRumb;
	}

	public void setWindRumb(int windRumb) {
		this.windRumb = windRumb;
	}

	public int getHumidityMin() {
		return humidityMin;
	}

	public void setHumidityMin(int humidityMin) {
		this.humidityMin = humidityMin;
	}

	public int getHumidityMax() {
		return humidityMax;
	}

	public void setHumidityMax(int humidityMax) {
		this.humidityMax = humidityMax;
	}

	public int getWpi() {
		return wpi;
	}

	public void setWpi(int wpi) {
		this.wpi = wpi;
	}

	@Override
	public String toString() {
		return "DayForecast [date=" + date + ", hour=" + hour + ", cloudId="
				+ cloudId + ", pictureName=" + pictureName + ", ppcp=" + ppcp
				+ ", temperatureMin=" + temperatureMin + ", temperatureMax="
				+ temperatureMax + ", pressureMin=" + pressureMin
				+ ", pressureMax=" + pressureMax + ", windMin=" + windMin
				+ ", windMax=" + windMax + ", windRumb=" + windRumb
				+ ", humidityMin=" + humidityMin + ", humidityMax="
				+ humidityMax + ", wpi=" + wpi + "]";
	}

	
}
