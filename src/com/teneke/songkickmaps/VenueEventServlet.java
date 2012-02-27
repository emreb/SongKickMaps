package com.teneke.songkickmaps;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class VenueEventServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/html");
		if (req.getParameter("id") != null) {

			long venueId = Long.parseLong(req.getParameter("id"));
			resp.getWriter().println(SongKickAPI.getVenueData(venueId, 1));
			// System.out.println("Getting:" + minLat + "x" + minLon + ":" + maxLat
			// + ":" + maxLon);
		} else {
			resp.getWriter().println("JACK");
		}

	}
}
