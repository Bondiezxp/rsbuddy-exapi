package com.rsbuddy.script.graphics;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Ramus
 */
public class Format {

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
	 * Formats the specified double with commas and the given amount of decimal
	 * places.
	 * 
	 * @param num
	 *            The double to format.
	 * @return The formatted double.
	 */
	public static String formatCommas(final int num) {
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
	public static String formatTime(final long millis, final TimeFormat format) {
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
		formatted = formatted.replaceFirst("0#", "" + (hours.length() > 1 ? String.valueOf(hours) : "0" + hours));
		formatted = formatted.replaceFirst("0#", "" + (minutes.length() > 1 ? String.valueOf(minutes) : "0" + minutes));
		formatted = formatted.replaceFirst("0#", "" + (seconds.length() > 1 ? String.valueOf(seconds) : "0" + seconds));
		formatted = formatted.replaceFirst("#", "" + hours);
		formatted = formatted.replaceFirst("#", "" + minutes);
		formatted = formatted.replaceFirst("#", "" + seconds);
		if (hours.equals("0")) {
			formatted = formatted.substring(formatted.indexOf("Hour") + (formatted.contains("Hours") ? 6 : 5));
		}
		if (minutes.equals("0")) {
			formatted = formatted.substring(formatted.indexOf("Minute") + (formatted.contains("Minutes") ? 8 : 7));
		}
		if (seconds.equals("0")) {
			formatted = formatted.substring(formatted.indexOf("Second") + (formatted.contains("Seconds") ? 8 : 7));
		}
		if (formatted.replaceAll(" ", "").isEmpty() || formatted.replace(" ", "").equals("")) {
			return "";
		}
		return formatted;
	}

	/**
	 * Formats the specified number in a money format. For example, 1500 would
	 * result in 1.5k.
	 * 
	 * @param num
	 *            The number to format.
	 * @return The number in a money format.
	 */
	public static String shortFormat(final int num) {
		if (num < 1000) {
			return String.valueOf(num);
		}
		final String str = Integer.toString(num);
		final int topsize = (str.length() + 2) % 3;
		String topstr = str.substring(0, topsize + 1);
		char dec;
		try {
			dec = str.charAt(topsize + 1);
		} catch (final StringIndexOutOfBoundsException e) {
			dec = ' ';
		}
		final int in = (str.length() - topsize) / 3;
		final char end = in == 1 ? 'k' : in == 2 ? 'm' : in == 3 ? 'b' : ' ';
		return topstr += (dec != ' ' && dec != '0' ? "." + dec : "") + (end != ' ' ? end : "");
	}
}