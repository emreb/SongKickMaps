package com.teneke.songkickmaps;

import java.util.ArrayList;

import com.teneke.songkickmaps.model.City;
import com.teneke.songkickmaps.model.Spot;

public class LocationService {

	private ArrayList<City> cities = null;

	private static LocationService _instance = null;

	private LocationService() {
		// {name: "NewYork", lat: 40.7512, lon: -73.9885},
		// {name: "London", lat: 51.50832, lon: -0.12774},
		// {name: "SanFrancisco", lat: 37.7770, lon: -122.4176},
		// {name: "Boston", lat: 42.3592, lon: -71.0591},
		// {name: "Chicago", lat: 41.8789, lon: -87.6291}];
		// London, SF, NYC, Boston, Chicago
		// int[] cities = { 24426, 26330, 7644, 18842, 9426 };

		cities = new ArrayList();

		final City london = new City("London", 24426, 51.50832, -0.12774);
		final City newyork = new City("NewYork", 7644, 40.7512, -73.9885);
		final City sanfrancisco = new City("SanFrancisco", 26330, 37.7770,
				-122.4176);
		final City boston = new City("Boston", 18842, 42.3592, -71.0591);
		final City chicago = new City("Chicago", 9426, 41.8789, -87.6291);
		final City losangeles = new City("LosAngeles", 17835, 34.05227, -118.24304);

		cities.add(london);
		cities.add(newyork);
		cities.add(losangeles);
		cities.add(sanfrancisco);
		cities.add(boston);
		cities.add(chicago);

	}

	public static LocationService getInstance() {
		if (_instance == null) {
			_instance = new LocationService();
		}
		return _instance;
	}

	public ArrayList<City> getCities() {
		return cities;
	}

	public City nearestCity(Spot s) {

		City nearest = null;
		double minDistance = 0.0;
		for (int i = 0; i < cities.size(); i++) {
			Spot c = new Spot(cities.get(i).getLat(), cities.get(i).getLon(), cities
					.get(i).getId());
			if (i == 0) {
				nearest = cities.get(i);
				minDistance = GetDistance(c, s);
			} else {
				double distance = GetDistance(c, s);
				if (distance < minDistance) {
					nearest = cities.get(i);
					minDistance = distance;
				}
			}
		}
		return nearest;
	}

	public City getCity(long metroCode) {
		for (City c : cities) {
			if (c.getId() == metroCode) {
				return c;
			}
		}
		return null;
	}

	public City getCity(String name) {
		for (City c : cities) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public static double GetDistance(Spot a, Spot b) {

		double lat = Math.pow((a.getLat() - b.getLat()), 2);
		double lon = Math.pow((a.getLon() - b.getLon()), 2);
		return Math.sqrt(lat + lon);

	}
}
