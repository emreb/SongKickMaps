package com.teneke.songkickmaps.model;

import java.io.Serializable;
import java.util.LinkedList;

public class QuadTree<Key extends Comparable<Spot>> implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	private Node root;

	// helper node data type
	private class Node implements Serializable {

		Spot s; // x- and y- coordinates
		Node NW, NE, SE, SW; // four subtrees
		long count = 1; // Count of objects beyond this point

		Node(Spot incoming) {

			this.s = incoming;
		}

	}

	public long size() {
		return root.count;
	}

	public void insert(Spot s) {

		root = insert(root, s);
	}

	private Node insert(Node h, Spot s) {

		if (h == null) {
			return new Node(s);
		} else if (less(s.getLon(), h.s.getLon()) && less(s.getLat(), h.s.getLat())) {
			h.SW = insert(h.SW, s);
		} else if (less(s.getLon(), h.s.getLon())
				&& !less(s.getLat(), h.s.getLat())) {
			h.NW = insert(h.NW, s);
		} else if (!less(s.getLon(), h.s.getLon())
				&& less(s.getLat(), h.s.getLat())) {
			h.SE = insert(h.SE, s);
		} else if (!less(s.getLon(), h.s.getLon())
				&& !less(s.getLat(), h.s.getLat()) && s.getId() != h.s.getId()) {
			h.NE = insert(h.NE, s);
		} else {
			h.count = h.count - 1;
		}
		h.count = h.count + 1;
		return h;
	}

	public LinkedList<Spot> query2D(Interval2D rect) {

		LinkedList<Spot> rv = new LinkedList<Spot>();
		query2D(root, rect, rv);
		return rv;
	}

	private void query2D(Node h, Interval2D rect, LinkedList<Spot> rv) {

		if (h == null) {
			return;
		}
		Double xmin = rect.SW.getLon();
		Double ymin = rect.SW.getLat();
		Double xmax = rect.NE.getLon();
		Double ymax = rect.NE.getLat();
		if (rect.contains(h.s)) {
			rv.add(h.s);
		}
		if (less(xmin, h.s.getLon()) && less(ymin, h.s.getLat())) {
			query2D(h.SW, rect, rv);
		}
		if (less(xmin, h.s.getLon()) && !less(ymax, h.s.getLat())) {
			query2D(h.NW, rect, rv);
		}
		if (!less(xmax, h.s.getLon()) && less(ymin, h.s.getLat())) {
			query2D(h.SE, rect, rv);
		}
		if (!less(xmax, h.s.getLon()) && !less(ymax, h.s.getLat())) {
			query2D(h.NE, rect, rv);
		}
	}

	private boolean less(Double k1, Double k2) {

		return k1.compareTo(k2) < 0;
	}

	private boolean eq(Double k1, Double k2) {

		return k1.compareTo(k2) == 0;
	}

}
