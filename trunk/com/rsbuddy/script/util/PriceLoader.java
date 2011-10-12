package com.rsbuddy.script.util;

import com.rsbuddy.script.methods.WebAPI;
import com.rsbuddy.script.methods.WebAPI.ItemEx;

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
		if (item == null || item.getGuidePrice() < 0) {
			final ItemEx itemEx = WebAPI.getItemInfo(itemId);
			if (itemEx == null || itemEx.getGePrice() < 0) {
				return -1;
			}
			return itemEx.getGePrice();
		}
		return item.getGuidePrice();
	}
}