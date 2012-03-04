package com.teneke.songkickmaps.db;

import java.util.Collections;
import java.util.LinkedList;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.teneke.songkickmaps.LocationService;
import com.teneke.songkickmaps.model.City;
import com.teneke.songkickmaps.model.QuadTree;

public class MemCacheDB {

	private static Cache _instance;

	private MemCacheDB() {

	}

	public static Cache getInstance() {
		if (_instance == null) {
			try {
				CacheFactory cacheFactory = CacheManager.getInstance()
						.getCacheFactory();
				_instance = cacheFactory.createCache(Collections.emptyMap());
			} catch (CacheException e) {
				// ...
			}
		}
		return _instance;
	}

	public static void putCitySpots(long cityCode, QuadTree q) {

		MemCacheDB.getInstance().put(cityCode, q);
		updateLoadedCities(LocationService.getInstance().getCity(cityCode));

	}

	public static QuadTree getCitySpots(long cityCode) {
		QuadTree spots = (QuadTree) MemCacheDB.getInstance().get(cityCode);
		if (spots == null) {
			spots = new QuadTree();
		}
		return spots;
	}

	public synchronized static void updateLoadedCities(City c) {
		LinkedList<City> cities = (LinkedList<City>) MemCacheDB.getInstance().get(
				"LOADED_CITIES");
		if (cities == null) {
			cities = new LinkedList<City>();
		}
		cities.remove(c);
		cities.add(c);
		MemCacheDB.getInstance().put("LOADED_CITIES", c);
	}

	public static LinkedList<City> getLoadedCities() {
		LinkedList<City> cities = (LinkedList<City>) MemCacheDB.getInstance().get(
				"LOADED_CITIES");
		return cities;
	}
}
