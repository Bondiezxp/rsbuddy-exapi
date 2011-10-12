package com.rsbuddy.script.graphics;

import java.awt.Color;

/**
 * @author Ramus
 */
public class Util {

	/**
	 * One hour in milliseconds.
	 */
	private static final double ONE_HOUR = 3600000D;

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
}
