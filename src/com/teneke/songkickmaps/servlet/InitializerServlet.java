package com.teneke.songkickmaps.servlet;import java.io.IOException;import javax.servlet.http.HttpServlet;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import com.teneke.songkickmaps.Databases;@SuppressWarnings("serial")public class InitializerServlet extends HttpServlet {	@Override	public void doGet(HttpServletRequest req, HttpServletResponse resp)			throws IOException {		resp.setContentType("text/plain");		try {			System.out.println("Resetting cities");			Databases.resetDatabases();			resp.getWriter().println("Cities Reset");		} catch (Exception e) {			resp.getWriter().println("Error in resetting");		}	}	// @Override	// public void init() throws ServletException {	//	// System.out.println("Loading up cities");	// MemCachePopulator.populateAllTheCities();	//	// System.out.println("Done loading cities");	//	// }}