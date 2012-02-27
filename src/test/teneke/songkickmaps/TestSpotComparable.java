package test.teneke.songkickmaps;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;

import com.teneke.songkickmaps.Spot;

public class TestSpotComparable {

  Spot a = new Spot(1.0, 1.0, 5);
  Spot b = new Spot(2.0, 2.0, 2);
  Spot c = new Spot(3.0, 3.0, 1);

  Spot d = new Spot(2.1, 2.1, 90);
  Spot e = new Spot(2.1000001, 2.1, 50);

  Spot p = new Spot(11.0, 30.0, 1);
  Spot p20 = new Spot(20.0, 20.0, 1);
  Spot p30 = new Spot(30.0, 30.0, 1);

  Spot t = new Spot(10.00, 10.00, 1);
  Spot t11 = new Spot(11.00, 10.00, 1);
  Spot t12 = new Spot(10.00, 11.00, 1);

  Spot s5020 = new Spot(50.0, 20.0, 1);
  Spot s0021 = new Spot(0.0, 21.0, 1);

  @Test
  public void testComparable() throws JSONException {

    Assert.assertEquals(-1, a.compareTo(b));
    Assert.assertEquals(-1, a.compareTo(c));
    Assert.assertEquals(-1, b.compareTo(c));
    Assert.assertEquals(1, c.compareTo(a));
    Assert.assertEquals(1, c.compareTo(b));
    Assert.assertEquals(1, b.compareTo(a));

    Assert.assertEquals(1, d.compareTo(b));

    Assert.assertEquals(1, e.compareTo(d));
    Assert.assertEquals(-1, d.compareTo(e));

    Assert.assertEquals(-1, p20.compareTo(p));
    Assert.assertEquals(1, p30.compareTo(p));

    Assert.assertEquals(1, t11.compareTo(t));
    Assert.assertEquals(1, t12.compareTo(t));

    // THIS IS THE PROBLEM. They are both less than the other
    Assert.assertEquals(-1, s5020.compareTo(s0021));
    Assert.assertEquals(-1, s0021.compareTo(s5020));
  }
}
