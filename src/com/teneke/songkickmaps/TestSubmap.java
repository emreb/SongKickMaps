package com.teneke.songkickmaps;

import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

public class TestSubmap {

	TreeMap<Spot, Integer> db = new TreeMap();

	@Before
	public void testPopulate() {
		int id = 1;
		for (double i = 0; i < 10; i = i + 1) {
			for (double j = 0; j < 10; j = j + 1) {
				Spot s = new Spot(i, j, id);
				db.put(s, id);
				id++;
			}
		}
	}

	@Test
	public void testSearch() throws JSONException {

		Spot fromKey = new Spot(3.0, 3.0, 0);
		Spot toKey = new Spot(4.0, 4.0, 0);
		SortedMap<Spot, Integer> s = db.subMap(fromKey, toKey);
		System.out.println(s.size());
		for (Spot t : s.keySet()) {
			System.out.println(t.getId() + ":" + t.getLat() + ":" + t.getLon());
		}
	}
}
