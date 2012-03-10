package com.teneke.songkickmaps;

import java.util.ArrayList;

import com.teneke.songkickmaps.model.City;
import com.teneke.songkickmaps.model.Spot;

public class LocationService {

  private ArrayList<City> cities = null;
  private static final double MAX_CITY_RANGE = 0.7103247578572764;

  private static LocationService _instance = null;

  private LocationService() {

    cities = new ArrayList<City>();

    final City london = new City("London", 24426, 51.50832, -0.12774);
    final City newyork = new City("NewYork", 7644, 40.7512, -73.9885);
    final City sanfrancisco =
        new City("SanFrancisco", 26330, 37.7770, -122.4176);
    final City boston = new City("Boston", 18842, 42.3592, -71.0591);
    final City chicago = new City("Chicago", 9426, 41.8789, -87.6291);
    final City losangeles = new City("LosAngeles", 17835, 34.05227, -118.24304);
    final City paris = new City("Paris", 28909, 48.85672, 2.35242);
    final City philadelphia =
        new City("Philadelphia", 5202, 39.95263, -75.16346);
    final City washington = new City("Washington", 1409, 38.9005, -77.0362);
    final City miami = new City("Miami", 9776, 25.7891, -80.2257);
    final City berlin = new City("Berlin", 28443, 52.52677, 13.4069);

    cities.add(london);
    cities.add(sanfrancisco);
    cities.add(newyork);
    cities.add(losangeles);
    cities.add(philadelphia);
    cities.add(boston);
    cities.add(chicago);
    cities.add(washington);
    cities.add(paris);
    cities.add(miami);
    cities.add(berlin);

  }

  public static LocationService getInstance() {

    if (_instance == null) {
      _instance = new LocationService();
    }
    return _instance;
  }

  public ArrayList<City> getCities() {

    return cities;
  }

  public City nearestCity(Spot s) {

    City nearest = null;
    double minDistance = 0.0;
    for (int i = 0; i < cities.size(); i++) {
      Spot c =
          new Spot(cities.get(i).getLat(), cities.get(i).getLon(), cities
              .get(i).getId());
      if (i == 0) {
        nearest = cities.get(i);
        minDistance = GetDistance(c, s);
      } else {
        double distance = GetDistance(c, s);
        if (distance < minDistance) {
          nearest = cities.get(i);
          minDistance = distance;
        }
      }
    }
    return nearest;
  }

  public City getCity(long metroCode) {

    for (City c : cities) {
      if (c.getId() == metroCode) {
        return c;
      }
    }
    return null;
  }

  public City getCity(String name) {

    for (City c : cities) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }

  public static double GetDistance(Spot a, Spot b) {

    double lat = Math.pow((a.getLat() - b.getLat()), 2);
    double lon = Math.pow((a.getLon() - b.getLon()), 2);
    return Math.sqrt(lat + lon);
  }

  public static double GetDistance(City c, Spot s) {

    Spot a = new Spot(c.getLat(), c.getLon(), 1);
    return GetDistance(a, s);
  }

  public static void main(String[] args) {

    Spot ny = new Spot(40.7512, -73.9885, 1);
    Spot pa = new Spot(39.95263, -75.16346, 1);
    System.out.println(GetDistance(ny, pa) / 2);
  }

  public static boolean isLessThanRange(City c, Spot v) {

    if (GetDistance(c, v) < MAX_CITY_RANGE) {
      return true;
    } else {
      return false;
    }
  }

}
