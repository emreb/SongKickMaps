package com.teneke.songkickmaps.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teneke.songkickmaps.LocationService;
import com.teneke.songkickmaps.SongKickAPI;
import com.teneke.songkickmaps.model.City;
import com.teneke.songkickmaps.model.Interval2D;
import com.teneke.songkickmaps.model.QuadTree;
import com.teneke.songkickmaps.model.Spot;

public class SpotDb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QuadTree<Spot> spotDB = new QuadTree<Spot>();
	private HashMap<Long, City> loadedCities = new HashMap<Long, City>();

	private static SpotDb _instance;

	public static SpotDb getInstance() {
		if (_instance == null) {
			_instance = new SpotDb();
		}
		return _instance;
	}

	public void storeVenueToMemory(double lat, double lon, long id,
			JSONObject data) {

		Spot s = new Spot(lat, lon, id);
		spotDB.insert(s);
		try {
			if (data.get("metroArea") != JSONObject.NULL) {
				data.remove("metroArea");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

	public QuadTree<Spot> getDb() {
		return spotDB;
	}

	public void setDb(QuadTree<Spot> d) {
		this.spotDB = d;
	}

	public void resetDatabases() {

		spotDB = new QuadTree<Spot>();

	}

	public void populateDatabase(long metroCode) throws JSONException {

		populateDatabase(metroCode, 1);
		loadedCities.put(metroCode, LocationService.getInstance()
				.getCity(metroCode));

	}

	public JSONArray venueListByGeo(Double minLat, Double maxLat, Double minLon,
			Double maxLon) {

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

	public void populateDatabase(long metroCode, int page) throws JSONException {

		JSONObject data = new JSONObject(SongKickAPI.getMetroData(metroCode, page));
		JSONObject resultsPage = data.getJSONObject("resultsPage");
		JSONObject results = resultsPage.getJSONObject("results");
		if (results.has("event")) {
			JSONArray events = results.getJSONArray("event");
			for (int i = 0; i < events.length(); i++) {
				JSONObject event = events.getJSONObject(i);
				JSONObject venue = event.getJSONObject("venue");
				if (venue != JSONObject.NULL && venue.get("lat") != JSONObject.NULL
						&& venue.get("lng") != JSONObject.NULL) {
					long venueId = -1;
					try {
						venueId = venue.getLong("id");
					} catch (Exception e) {
						// System.err.println("Error in parsing:" + venue);
					}
					if (venueId != -1) {
						try {
							storeVenueToMemory(venue.getDouble("lat"),
									venue.getDouble("lng"), venue.getLong("id"), venue);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			page++;
			populateDatabase(metroCode, page);
		}

	}

	public static void main(String[] args) {

		SpotDb spots = new SpotDb();
		try {
			spots.populateDatabase(24426);
			JSONArray venues = spots.venueListByGeo(51.4846, 51.5067, -0.1778,
					-0.1346);
			int i = 1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}