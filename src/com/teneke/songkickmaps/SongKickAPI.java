package com.teneke.songkickmaps;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SongKickAPI {

  private static String API_KEY = "apikey=" + SongKickAPIKey.key;

  private final static String BASE_URL = "http://api.songkick.com/api/3.0";

  private final static String METRO_AREA_CALENDAR =
      "/metro_areas/%/calendar.json?";

  private final static String VENUE_CALENDAR = "/venues/%/calendar.json?";

  private final static String PAGINATION = "&page=%";

  private static String readAPI(String s) {

    String inputLine = "";
    StringBuffer output = new StringBuffer();
    try {
      URL sk = new URL(s);
      URLConnection skConnection = sk.openConnection();
      DataInputStream dis = new DataInputStream(skConnection.getInputStream());
      while ((inputLine = dis.readLine()) != null) {
        output.append(inputLine);
      }
      dis.close();
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return output.toString();
  }

  public static String getMetroData(int metroCode, int page) {

    String metroURL = METRO_AREA_CALENDAR.replace("%", "" + metroCode);
    String pagination = PAGINATION.replace("%", "" + page);
    String url = BASE_URL + metroURL + API_KEY + pagination;
    return readAPI(url);
  }

  public static String getVenueData(long venueId, int page) {

    String venueURL = VENUE_CALENDAR.replace("%", "" + venueId);
    String pagination = PAGINATION.replace("%", "" + page);
    String url = BASE_URL + venueURL + API_KEY + pagination;
    return readAPI(url);
  }
}
