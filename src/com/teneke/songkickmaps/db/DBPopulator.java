package com.teneke.songkickmaps.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.teneke.songkickmaps.LocationService;
import com.teneke.songkickmaps.model.City;
import com.teneke.songkickmaps.model.QuadTree;
import com.teneke.songkickmaps.model.Spot;

public class DBPopulator {

	// Get Cities
	// Put Them in memcache
	// Update the list of cities in memcache

	private static final Logger logger = Logger.getLogger(DBPopulator.class
			.getName());

	public static SpotDb populateAllCities() {

		SpotDb citySpotDb = new SpotDb();
		for (City c : LocationService.getInstance().getCities()) {
			try {
				logger.log(Level.INFO, "DB Populating " + c.getName());
				citySpotDb.populateDatabase(c.getId());
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error while DB populating " + c.getName(), e);
			}
		}

		return citySpotDb;

	}

	// create user 'appuser'@'%' identified by 'muzikbaymaps'
	// create table venuedb (
	// id BIGINT(20) PRIMARY KEY,
	// data BLOB)
	public static long storeLatestToDb() {
		SpotDb latest = populateAllCities();
		long timeStamp = System.currentTimeMillis();
		Connection c = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = DriverManager
					.getConnection("jdbc:google:rdbms://songkickmapsproject:musicbymapdb/mbm");

			String statement = "INSERT INTO mbm (id, data) VALUES( ? , ? )";
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setLong(1, timeStamp);
			stmt.setString(2, latest.toString());
			int success = 2;
			success = stmt.executeUpdate();
			if (success == 1) {
				logger.info("All good in storing");
			} else if (success == 0) {
				logger.severe("Something went bad");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
				}
		}
		return timeStamp;
	}

	public static QuadTree<Spot> getSpotDbForInstance() {

		// find all the cities in memcache
		LinkedList<City> cities = MemCacheDB.getLoadedCities();
		logger.info(cities.size() + " cities loaded from memcache index");
		// Append to a single quatree
		QuadTree<Spot> spots = new QuadTree<Spot>();
		for (City c : cities) {
			logger.info("Loading " + c.getName());
			QuadTree<Spot> cityDb = MemCacheDB.getCitySpots(c.getId());
			try {
				spots.insertSpotDB(cityDb);
			} catch (Exception a) {
				logger.log(Level.SEVERE, "Cannot insert into main spot DB data for "
						+ c.getName(), a);
			}
		}
		// Return quadtree for servlet use
		return spots;
	}

	public static void main(String[] args) {

		// populateAllCities();
		storeLatestToDb();
	}

}
