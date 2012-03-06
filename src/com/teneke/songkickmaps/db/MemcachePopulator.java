package com.teneke.songkickmaps.db;

import java.util.LinkedList;

import org.json.JSONException;

import com.teneke.songkickmaps.LocationService;
import com.teneke.songkickmaps.model.City;
import com.teneke.songkickmaps.model.QuadTree;
import com.teneke.songkickmaps.model.Spot;

public class MemCachePopulator {

	// Get Cities
	// Put Them in memcache
	// Update the list of cities in memcache

	public static void populateMemcacheWithCity(City c) {
		SpotDb citySpotDb = new SpotDb();
		try {
			citySpotDb.populateDatabase(c.getId());
			MemCacheDB.putCitySpots(c.getId(), citySpotDb.getDb());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void populateAllTheCities() {
		// Get the city list and then call one by one

		for (City c : LocationService.getInstance().getCities()) {
			populateMemcacheWithCity(c);
		}

	}

	public static QuadTree<Spot> getSpotDbForInstance() {
		// find all the cities in memcache
		LinkedList<City> cities = MemCacheDB.getLoadedCities();
		// Append to a single quatree
		QuadTree<Spot> spots = new QuadTree<Spot>();
		for (City c : cities) {
			QuadTree<Spot> cityDb = MemCacheDB.getCitySpots(c.getId());
			spots.insertSpotDB(cityDb);
		}
		// Return quadtree for servlet use
		return spots;
	}

	public static void main(String[] args) {
		populateAllTheCities();
	}

}
