package test.teneke.songkickmaps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.teneke.songkickmaps.Databases;
import com.teneke.songkickmaps.db.SpotDb;

public class TestDatabases {

	SpotDb spots = new SpotDb();

	@Before
	public void testPopulate() throws JSONException {
		int id = 1;
		for (double i = 0; i < 10; i = i + 1) {
			for (double j = 0; j < 10; j = j + 1) {
				JSONObject jo = new JSONObject();
				jo.put("lat", i);
				jo.put("lng", j);
				jo.put("id", id);
				spots.storeVenueToMemory(i, j, id, jo);
				id++;
			}
		}
	}

	@Test
	public void testSearch() throws JSONException {

		JSONArray j = Databases.venueListByGeo(3.0, 4.0, 3.0, 4.0);
		System.out.println(j.length());
		for (int i = 0; i < j.length(); i++) {
			System.out.println(j.getJSONObject(i).toString());
		}
	}
}
