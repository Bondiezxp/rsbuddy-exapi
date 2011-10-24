package com.rsbuddy.script.methods;

import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Locatable;
import com.rsbuddy.script.wrappers.Targetable;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author Ramus
 */
public class ExCalculations {

	/**
	 * Gets a random point of the specified Locatable.
	 * 
	 * @param locatable
	 *            The <tt>Locatable</tt> to get a random point of.
	 * @return A random point in the Locatable.
	 */
	public static Point getRandomPoint(final Locatable locatable) {
		final Point p = locatable.getLocation().getPoint(0, 1, 0);
		final int x = p.x;
		final int y = p.y;
		final int width = locatable.getLocation().getPoint(1, 1, 0).x - x;
		final int height = locatable.getLocation().getPoint(1, 1, 0).y - y;
		if (x == -1 || y == -1 || width == -1 || height == -1) {
			return null;
		}
		return new Point(Random.nextGaussian(x, x + width, 2), Random.nextGaussian(y, y + height, 2));
	}

	/**
	 * Gets a random point in the specified rectangle.
	 * 
	 * @param rect
	 *            The rectangle to get a random point of.
	 * @return A random point in the rectangle.
	 */
	public static Point getRandomPoint(final Rectangle rect) {
		return new Point(Random.nextGaussian(rect.x, rect.x + rect.width, 2), Random.nextGaussian(rect.y, rect.y
				+ rect.height, 2));
	}

	/**
	 * Gets a random point in the specified model.
	 * 
	 * @param target
	 *            The model to get a random point of.
	 * @return A random point in the model.
	 */
	public static Point getRandomPoint(final Targetable target) {
		int x = -1;
		int y = -1;
		int width = -1;
		int height = -1;
		for (int i = 500; i > 0; i -= 1) {
			final Point center = target.getCenterPoint();
			if (x != -1 && y != -1 && width != -1 && height != -1) {
				break;
			}
			if (x == -1 && target.contains(new Point(center.x - i, center.y))) {
				x = center.x - i;
			}
			if (y == -1 && target.contains(new Point(center.x, center.y - i))) {
				y = center.y - i;
			}
			if (width == -1 && target.contains(new Point(center.x + i, center.y))) {
				width = i;
			}
			if (height == -1 && target.contains(new Point(center.x, center.y + i))) {
				height = i;
			}
		}
		if (x == -1 || y == -1 || width == -1 || height == -1) {
			return null;
		}
		return new Point(Random.nextGaussian(x, x + width, 2), Random.nextGaussian(y, y + height, 2));
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
			if (point == null || point.x > Game.getCanvasSize().width || point.y > Game.getCanvasSize().height
					|| point.x < 0 || point.y < 0 || !Calculations.isPointOnScreen(point)) {
				return false;
			}
		}
		return true;
	}
}