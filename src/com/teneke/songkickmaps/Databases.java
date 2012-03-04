package com.teneke.songkickmaps;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teneke.songkickmaps.model.Interval2D;
import com.teneke.songkickmaps.model.QuadTree;
import com.teneke.songkickmaps.model.Spot;

public class Databases {

	private static HashMap<Long, JSONObject> venueDB = new HashMap<Long, JSONObject>();

	private static QuadTree<Spot> spotDB = new QuadTree<Spot>();

	// Get instance, singleton
	// Load from memcache the singleton
	// Make sure there is a way to signal force refresh
	// Make sure part of preloading takes care of this, init

	public static void resetDatabases() {

		venueDB = new HashMap<Long, JSONObject>();
		spotDB = new QuadTree<Spot>();

	}

	public static JSONObject getVenue(long id) {

		return venueDB.get(id);
	}

	public static JSONArray venueListByGeo(Double minLat, Double maxLat,
			Double minLon, Double maxLon) {

		Spot min = new Spot(minLat, minLon, 1);
		Spot max = new Spot(maxLat, maxLon, 1);
		Interval2D range = new Interval2D(min, max);
		LinkedList<Spot> spots = spotDB.query2D(range);
		JSONArray rv = new JSONArray();
		for (Spot s : spots) {
			rv.put(venueDB.get(s.getId()));
		}
		return rv;
	}

	public static void main(String[] args) {

		try {
			populateDatabases(24426);
			JSONArray venues = venueListByGeo(51.4846, 51.5067, -0.1778, -0.1346);
			int i = 1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}