package com.rsbuddy.script.methods;

import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Model;
import com.rsbuddy.script.wrappers.Tile;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;

public class ExCalculations {

	public static class Paint {

		/**
		 * Used for formatting longs to readable Strings.
		 */
		public static enum TimeFormat {

			DIGITAL("0#:0#"),
			FULL_DIGITAL("0#:0#:0#"),
			FULL_TEXT("# Hour # Minute # Second"),
			TEXT("# Hour # Minute");

			private final String format;

			private TimeFormat(final String format) {
				this.format = format;
			}

			/**
			 * Gets the time format.
			 * 
			 * @return The time format as a string.
			 */
			public String getFormat() {
				return format;
			}
		}

		/**
		 * One hour in milliseconds.
		 */
		private static final double ONE_HOUR = 3600000D;

		/**
		 * Formats the specified double with commas and the given amount of
		 * decimal places.
		 * 
		 * @param num
		 *            The double to format.
		 * @return The formatted double.
		 */
		public static String formatComas(final int num) {
			final NumberFormat numberFormat = new DecimalFormat("#,###,###,###");
			return numberFormat.format(num);
		}

		/**
		 * Formats the specified long with the given time format.
		 * 
		 * @param millis
		 *            The long to format.
		 * @param format
		 *            The String to format the long with.
		 * @return The formatted readable long.
		 */
		public static String formatTime(final long millis,
				final TimeFormat format) {
			if (millis < 1000) {
				return "";
			}
			final long time = millis / 1000;
			final String seconds = String.valueOf((int) (time % 60));
			final String minutes = String.valueOf((int) (time % 3600 / 60));
			final String hours = String.valueOf((int) (time / 3600));
			String formatted = format.getFormat();
			if (!hours.equals("1")) {
				formatted = formatted.replaceFirst("Hour", "Hours");
			}
			if (!minutes.equals("1")) {
				formatted = formatted.replaceFirst("Minute", "Minutes");
			}
			if (!seconds.equals("1")) {
				formatted = formatted.replaceFirst("Second", "Seconds");
			}
			formatted = formatted.replaceFirst("0#",
					""
							+ (hours.length() > 1 ? String.valueOf(hours) : "0"
									+ hours));
			formatted = formatted.replaceFirst("0#", ""
					+ (minutes.length() > 1 ? String.valueOf(minutes) : "0"
							+ minutes));
			formatted = formatted.replaceFirst("0#", ""
					+ (seconds.length() > 1 ? String.valueOf(seconds) : "0"
							+ seconds));
			formatted = formatted.replaceFirst("#", "" + hours);
			formatted = formatted.replaceFirst("#", "" + minutes);
			formatted = formatted.replaceFirst("#", "" + seconds);
			if (hours.equals("0")) {
				formatted = formatted.substring(formatted.indexOf("Hour")
						+ (formatted.contains("Hours") ? 6 : 5));
			}
			if (minutes.equals("0")) {
				formatted = formatted.substring(formatted.indexOf("Minute")
						+ (formatted.contains("Minutes") ? 8 : 7));
			}
			if (seconds.equals("0")) {
				formatted = formatted.substring(formatted.indexOf("Second")
						+ (formatted.contains("Seconds") ? 8 : 7));
			}
			if (formatted.replaceAll(" ", "").isEmpty()
					|| formatted.replace(" ", "").equals("")) {
				return "";
			}
			return formatted;
		}

		/**
		 * Gets the per hour value of the given number and the run time.
		 * 
		 * @param runTime
		 *            The amount of time running.
		 * @param num
		 *            The number to get per hour.
		 * @return The per hour value of the given number and run time.
		 */
		public static int getPerHourValue(final long runTime, final int num) {
			if (num < 1 || runTime < 1) {
				return 0;
			}
			return (int) (ONE_HOUR / runTime * num);
		}

		/**
		 * Sets the alpha of the given color.
		 * 
		 * @param c
		 *            The color to set the alpha of.
		 * @param alpha
		 *            The alpha. 0 - 255 (Transparent - Opaque)
		 * @return The given color with the specified alpha.
		 */
		public static Color setAlpha(final Color c, final int alpha) {
			if (alpha < 0 || alpha > 255) {
				return c;
			}
			final int red = c.getRed();
			final int green = c.getGreen();
			final int blue = c.getBlue();
			return new Color(red, green, blue, alpha);
		}

		/**
		 * Formats the specified number in a money format. For example, 1500
		 * would result in 1.5k.
		 * 
		 * @param num
		 *            The number to format.
		 * @return The number in a money format.
		 */
		public static String shortFormat(final int num) {
			final String formatted = String.valueOf(num);
			if (formatted.length() < 4) {
				return formatted;
			}
			String sign = "k";
			int length = 3;
			if (formatted.length() > 6 && formatted.length() < 9) {
				sign = "m";
				length = 6;
			} else if (formatted.length() > 9) {
				sign = "b";
				length = 9;
			}
			if (formatted.charAt(formatted.length() - length) == '0') {
				return formatted.substring(0, formatted.length() - length)
						+ sign;
			}
			return formatted.substring(0, formatted.length() - length)
					+ "."
					+ formatted.substring(formatted.length() - length,
							formatted.length() - length + 1) + sign;
		}
	}

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
