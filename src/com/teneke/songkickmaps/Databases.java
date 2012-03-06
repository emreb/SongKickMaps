package com.teneke.songkickmaps;

import java.util.LinkedList;

import org.json.JSONArray;

import com.teneke.songkickmaps.db.MemCachePopulator;
import com.teneke.songkickmaps.model.Interval2D;
import com.teneke.songkickmaps.model.QuadTree;
import com.teneke.songkickmaps.model.Spot;

public class Databases {

	private static QuadTree<Spot> spotDB = MemCachePopulator
			.getSpotDbForInstance();

	// Get instance, singleton
	// Load from memcache the singleton
	// Make sure there is a way to signal force refresh
	// Make sure part of preloading takes care of this, init

	public static void resetDatabases() {

		spotDB = MemCachePopulator.getSpotDbForInstance();

	}

	// public static JSONObject getVenue(long id) {
	//
	// return venueDB.get(id);
	// }

	public static JSONArray venueListByGeo(Double minLat, Double maxLat,
			Double minLon, Double maxLon) {

		Spot min = new Spot(minLat, minLon, 1);
		Spot max = new Spot(maxLat, maxLon, 1);
		Interval2D range = new Interval2D(min, max);
		LinkedList<Spot> spots = spotDB.query2D(range);
		JSONArray rv = new JSONArray();
		for (Spot s : spots) {
			rv.put(s.toJson());
		}
		return rv;
	}

}