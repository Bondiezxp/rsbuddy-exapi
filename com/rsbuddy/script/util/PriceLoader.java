package com.rsbuddy.script.util;

import org.rsbuddy.net.GeItem;

public class PriceLoader {

	/**
	 * Gets the price of the specified item id.
	 * 
	 * @param itemId
	 *            The id of the item to get the price of.
	 * @return The price of the item.
	 */
	public static int getPrice(final int itemId) {
		final GeItem item = GeItem.lookup(itemId);
		if (item == null) {
			return -1;
		}
		return item.getGuidePrice();
	}
}