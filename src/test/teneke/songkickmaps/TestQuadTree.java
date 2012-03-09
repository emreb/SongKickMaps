package test.teneke.songkickmaps;

import java.util.LinkedList;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.teneke.songkickmaps.model.Interval2D;
import com.teneke.songkickmaps.model.QuadTree;
import com.teneke.songkickmaps.model.Spot;

public class TestQuadTree {

	QuadTree<Spot> st = new QuadTree<Spot>();

	@Before
	public void testPopulate() {

	}

	@Test
	public void testSearch() throws JSONException {

		int N = 10000; // points

		// insert N random points in the unit square
		for (int i = 0; i < N; i++) {
			Double x = (double) (100 * Math.random());
			Double y = (double) (100 * Math.random());
			// System.out.println("(" + x + ", " + y + ")");
			st.insert(new Spot(x, y, i));
		}
		System.out.println("Done preprocessing " + N + " points");

		Spot minSpot = new Spot(30.0, 40.0, 0);
		Spot maxSpot = new Spot(50.0, 50.0, 0);
		Interval2D range = new Interval2D(minSpot, maxSpot);
		LinkedList<Spot> results = st.query2D(range);
		for (Spot s : results) {
			Assert.assertTrue(minSpot.getLat() < s.getLat());
			Assert.assertTrue(minSpot.getLon() < s.getLon());
			Assert.assertTrue(maxSpot.getLat() > s.getLat());
			Assert.assertTrue(maxSpot.getLon() > s.getLon());
			System.out.println(s.getLat() + ", " + s.getLon() + " " + s.getId());
		}
	}

	@Test
	public void testSamePointDiffId() {
		Spot a = new Spot(10.0, 10.0, 1);
		Spot b = new Spot(10.0, 10.0, 2);
		Spot c = new Spot(10.0, 10.0, 1);
		QuadTree<Spot> s = new QuadTree<Spot>();
		s.insert(a);
		s.insert(b);
		s.insert(c);
		Assert.assertEquals(2, s.size());
	}
}
