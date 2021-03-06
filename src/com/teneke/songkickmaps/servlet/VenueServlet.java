package com.teneke.songkickmaps.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teneke.songkickmaps.Databases;

@SuppressWarnings("serial")
public class VenueServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    resp.setContentType("text/html");
    if (req.getParameter("minLat") != null
        && req.getParameter("maxLat") != null
        && req.getParameter("minLon") != null
        && req.getParameter("maxLon") != null) {

      double minLat = Double.parseDouble(req.getParameter("minLat"));
      double maxLat = Double.parseDouble(req.getParameter("maxLat"));
      double minLon = Double.parseDouble(req.getParameter("minLon"));
      double maxLon = Double.parseDouble(req.getParameter("maxLon"));
      resp.getWriter().println(
          Databases.venueListByGeo(minLat, maxLat, minLon, maxLon));
      // System.out.println("Getting:" + minLat + "x" + minLon + ":" + maxLat
      // + ":" + maxLon);
    } else {
      resp.getWriter().println("JACK");
    }

  }

  @Override
  public void init() throws ServletException {

    Databases.venueListByGeo(0.0, 0.0, 0.0, 0.0);
  }
}
