package com.teneke.songkickmaps;

public class Spot implements Comparable {

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
	public int compareTo(Object arg0) {

		Spot t = (Spot) arg0;
		int x = 0;
		int y = 0;
		if (this.lat > t.getLat()) {
			y = 1;
		}
		if (this.lon > t.getLon()) {
			x = 1;
		}

		if (x + y == 2) {
			return 1;
		} else {
			return -1;
		}

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Spot other = (Spot) obj;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		return true;
	}

}