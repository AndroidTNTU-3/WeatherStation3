package com.unrealedz.wstation.entity;

public class City {
	private int id;
	private String name;
	private String nameEn;
	private Region region;
	private Country country;

	public static class Region {
		private String region;
		private String regionEn;

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getRegionEn() {
			return regionEn;
		}

		public void setRegionEn(String regionEn) {
			this.regionEn = regionEn;
		}

		@Override
		public String toString() {
			return "Region [region=" + region + ", regionEn=" + regionEn + "]";
		}
	}

	public static class Country {
		private int countryId;
		private String country;
		private String countryEn;

		public int getCountryId() {
			return countryId;
		}

		public void setCountryId(int countryId) {
			this.countryId = countryId;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getCountryEn() {
			return countryEn;
		}

		public void setCountryEn(String countryEn) {
			this.countryEn = countryEn;
		}

		@Override
		public String toString() {
			return "Country [countryId=" + countryId + ", country=" + country
					+ ", countryEn=" + countryEn + "]";
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "City [id=" + String.valueOf(id) + ", name=" + name + ", nameEn=" + nameEn
				+ ", region=" + region + ", country=" + country + "]";
	}
}
