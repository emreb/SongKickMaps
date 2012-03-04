package com.teneke.songkickmaps.model;

import org.json.JSONObject;

public class Spot implements Comparable<Spot> {

	final double lat;
	final double lon;
	final long id;

	public Spot(double lat, double lon, long id) {

		this.lat = lat;
		this.lon = lon;
		this.id = id;
	}

	public double getLat() {

		return lat;
	}

	public double getLon() {

		return lon;
	}

	public long getId() {

		return id;
	}

	@Override
	public int compareTo(Spot t) {

		if (this.lon >= t.getLon()) {
			if (this.lat >= t.getLat()) {
				return 1;
			}
		} else if (this.equals(t)) {
			return 0;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Spot other = (Spot) obj;
		if (id != other.id) {
			return false;
		}
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat)) {
			return false;
		}
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon)) {
			return false;
		}
		return true;
	}

	public JSONObject toJson() {
		JSONObject j = new JSONObject();
		try {
			j.put("id", id);
			j.put("lat", lat);
			j.put("lon", lon);
		} catch (Exception e) {

		}
		return null;
	}

}