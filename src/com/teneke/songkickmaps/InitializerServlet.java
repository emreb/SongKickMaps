package com.teneke.songkickmaps;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class InitializerServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    resp.setContentType("text/plain");
    try {
      Databases.populateDatabases(24426);
      resp.getWriter().println("DB Initiliazed");
    } catch (Exception e) {
      resp.getWriter().println("Error in initizalizatin");
    }

  }
}
