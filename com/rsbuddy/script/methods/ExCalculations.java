package com.rsbuddy.script.methods;

import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Model;
import com.rsbuddy.script.wrappers.Tile;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.LinkedList;

public class ExCalculations {

	/**
	 * Gets the center point of a model.
	 * 
	 * @param model
	 *            The model.
	 * @return The center point of the model.
	 */
	public static Point getModelCenter(final Model model) {
		if (model == null) {
			return null;
		}
		final Polygon[] trianlges = model.getTriangles();
		final LinkedList<Point> points = new LinkedList<Point>();
		for (final Polygon triangle : trianlges) {
			for (int i = 0; i < triangle.npoints; i += 1) {
				points.add(new Point(triangle.xpoints[i], triangle.ypoints[i]));
			}
		}
		int xTotal = 0;
		int yTotal = 0;
		for (final Point p : points) {
			xTotal += p.x;
			yTotal += p.y;
		}
		if (points.size() <= 5) {
			return null;
		}
		return new Point(xTotal / points.size(), yTotal / points.size());
	}

	/**
	 * Gets a random point in the specified model.
	 * 
	 * @param model
	 *            The model to get a random point of.
	 * @return A random point in the model.
	 */
	public static Point getRandomPoint(final Model model) {
		if (model == null || model.getTriangles() == null
				|| model.getTriangles().length < 1) {
			return null;
		}
		final Polygon[] triangles = model.getTriangles();
		return getRandomPoint(triangles[Random.nextInt(0, triangles.length)]);
	}

	/**
	 * Gets a random point in the specified polygon.
	 * 
	 * @param p
	 *            The polygon to get a random point of.
	 * @return A random point in the polygon.
	 */
	public static Point getRandomPoint(final Polygon p) {
		double a = Math.random();
		double b = Math.random();
		if (a + b >= 1) {
			a = 1 - a;
			b = 1 - b;
		}
		final double c = 1 - a - b;
		return new Point((int) (a * p.xpoints[0] + b * p.xpoints[1] + c
				* p.xpoints[2]), (int) (a * p.ypoints[0] + b * p.ypoints[1] + c
				* p.ypoints[2]));
	}

	/**
	 * Gets a random point in the specified rectangle.
	 * 
	 * @param rect
	 *            The rectangle to get a random point of.
	 * @return A random point in the rectangle.
	 */
	public static Point getRandomPoint(final Rectangle rect) {
		return new Point(Random.nextInt(rect.x, rect.x + rect.width + 1),
				Random.nextInt(rect.y, rect.y + rect.height + 1));
	}

	/**
	 * Gets a random point of the specified tile.
	 * 
	 * @param tile
	 *            The tile to get a random point of.
	 * @return A random point in the tile.
	 */
	public static Point getRandomPoint(final Tile tile) {
		final Point bl = tile.getPoint(0, 0, 0);
		final Point br = tile.getPoint(1, 0, 0);
		final Point tr = tile.getPoint(1, 1, 0);
		final Point tl = tile.getPoint(0, 1, 0);
		return getRandomPoint(new Polygon(new int[] { bl.x, br.x, tr.x, tl.x },
				new int[] { bl.y, br.y, tr.y, tl.y }, 4));
	}

	/**
	 * Checks wether the specified point(s) is on screen.
	 * 
	 * @param points
	 *            The point(s) to check.
	 * @return <tt>true</tt> if <tt>all</tt> points are visible on the screen.
	 */
	public static boolean isPointOnScreen(final Point... points) {
		for (final Point point : points) {
			if (point == null || point.x > Game.getCanvasSize().width
					|| point.y > Game.getCanvasSize().height || point.x < 0
					|| point.y < 0 || !Calculations.isPointOnScreen(point)) {
				return false;
			}
		}
		return true;
	}
}
