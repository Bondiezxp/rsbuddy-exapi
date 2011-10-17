package com.rsbuddy.script.util;

import java.util.LinkedHashMap;

import org.rsbuddy.net.GeItem;

public class PriceLoader {

	private static final LinkedHashMap<Integer, Integer> CACHE = new LinkedHashMap<Integer, Integer>();

	/**
	 * Gets the price of the specified item id.
	 * 
	 * @param itemId
	 *            The id of the item to get the price of.
	 * @return The price of the item.
	 */
	public static int getPrice(final int itemId) {
		if (CACHE.containsKey(itemId)) {
			return CACHE.get(itemId);
		}
		final GeItem item = GeItem.lookup(itemId);
		if (item == null) {
			return -1;
		}
		final int price = item.getGuidePrice();
		CACHE.put(itemId, price);
		return price;
	}
}