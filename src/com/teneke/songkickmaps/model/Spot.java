package com.teneke.songkickmaps.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class Spot implements Comparable<Spot>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double lat;
	private double lon;
	private long id;
	private int nextEvent = 0; // 1 in 24 hours, 2 in 24-week, 3 more, 0 no data

	private static final long DAY = 60 * 60 * 24 * 1000L;
	private static final long WEEK = 7 * DAY;

	public Spot(double lat, double lon, long id) {

		this.lat = lat;
		this.lon = lon;
		this.id = id;

	}

	public void setNextEvent(String date, String time, String datetime) {
		System.out.println(date + ":" + time + ":" + datetime);
		DateFormat fullFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = fullFormatter.parse(datetime);
			long diff = d.getTime() - System.currentTimeMillis();
			if (diff < DAY) {
				nextEvent = 1;
			} else if (diff < WEEK) {
				nextEvent = 2;
			} else if (diff > WEEK) {
				nextEvent = 3;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}

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
			j.put("t", this.nextEvent);
		} catch (Exception e) {

		}
		return j;
	}

}