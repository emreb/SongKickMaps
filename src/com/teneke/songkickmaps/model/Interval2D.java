package com.teneke.songkickmaps.model;


public class Interval2D {

  public Spot SW, NE; // Aka min and max

  public Interval2D(Spot min, Spot max) {

    SW = min;
    NE = max;
  }

  public boolean contains(Spot s) {

    if (SW.getLat() < s.getLat() && SW.getLon() < s.getLon()
        && NE.getLat() > s.getLat() && NE.getLon() > s.getLon()) {
      return true;
    } else {
      return false;
    }

  }
}