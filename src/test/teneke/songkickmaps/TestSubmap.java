package test.teneke.songkickmaps;

import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.teneke.songkickmaps.Spot;

public class TestSubmap {

  TreeMap<Spot, Integer> db = new TreeMap();

  @Before
  public void testPopulate() {

    int id = 1;
    for (double i = 0; i <= 50; i = i + 1) {
      for (double j = 0; j <= 50; j = j + 1) {
        Spot s = new Spot(i, j, id);
        db.put(s, id);
        id++;
      }
    }
    // for (Spot t : db.keySet()) {
    // System.out.println(t.getId() + ":" + t.getLat() + ":" + t.getLon());
    // }
    // System.out.println("end all-------" + db.size());
  }

  @Test
  public void testSearch() throws JSONException {

    Spot fromKey = new Spot(20, 20, 0);
    Spot toKey = new Spot(30, 30, 0);
    SortedMap<Spot, Integer> s = db.subMap(fromKey, true, toKey, true);
    System.out.println(s.size());
    for (Spot t : s.keySet()) {
      System.out.println(t.getLat() + "," + t.getLon());
    }
  }
}
