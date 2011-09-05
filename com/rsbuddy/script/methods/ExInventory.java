package com.rsbuddy.script.methods;

import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Item;

import org.rsbuddy.tabs.Inventory;

public class ExInventory {

	/**
	 * Gets the first item excepting those matching with any of the provided
	 * ids.
	 * 
	 * @param leftToRight
	 *            <tt>true</tt> to span row by row (horizontal precedence);
	 *            <tt>false</tt> to span column by column (vertical precedence).
	 * @param ids
	 *            the item ids to exclude
	 */
	public static Item getItem(final boolean leftToRight, final int... ids) {
		if (!Inventory.containsOneOf(ids)) {
			return null;
		}
		if (!leftToRight) {
			for (int c = 0; c < 4; c += 1) {
				for (int r = 0; r < 7; r += 1) {
					boolean found = false;
					final Item item = Inventory.getAllItems()[c + r * 4];
					for (int i = 0; !found && i < ids.length; i += 1) {
						found = ids[i] == item.getId();
					}
					if (found) {
						return item;
					}
				}
			}
		} else {
			for (int r = 0; r < 7; r += 1) {
				for (int c = 0; c < 4; c += 1) {
					boolean found = false;
					final Item item = Inventory.getAllItems()[c + r * 4];
					for (int i = 0; !found && i < ids.length; i += 1) {
						found = ids[i] == item.getId();
					}
					if (found) {
						return item;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the first item vertically (going down the inventory) excepting those
	 * matching with any of the provided ids.
	 * 
	 * @param ids
	 *            the item ids to exclude
	 * @see #getItem(boolean, int...)
	 */
	public static Item getItem(final int... ids) {
		return getItem(false, ids);
	}

	/**
	 * Hovers the item at the specified slot for 1 - 2 seconds.
	 * 
	 * @param slot
	 *            The slot of the item.
	 * @return <tt>true</tt> if the item was hovered.
	 */
	public static boolean hoverItem(final int slot) {
		return hoverItem(slot, Random.nextInt(1000, 2000));
	}

	/**
	 * Hovers the item at the specified slot for the given amount of time.
	 * 
	 * @param slot
	 *            The slot of the item.
	 * @param time
	 *            The minimum amount of time in milliseconds to hover the item.
	 * @return <tt>true</tt> if the item was hovered.
	 */
	public static boolean hoverItem(final int slot, final int time) {
		return hoverItem(Inventory.getItemAt(slot), time);
	}

	/**
	 * Right clicks the item at the specified slot and hovers the menu action.
	 * 
	 * @param slot
	 *            The slot of the item.
	 * @param action
	 * @return <tt>true</tt> if the item was right clicked and the menu was
	 *         hovered.
	 */
	public static boolean hoverItem(final int slot, final String action) {
		if (!hoverItem(slot, 0)) {
			return false;
		}
		return ExMenu.hoverAction(action);
	}

	/**
	 * Hovers the given item for 1 - 2 seconds.
	 * 
	 * @param item
	 *            The item to hover.
	 * @return <tt>true</tt> if the item was hovered.
	 */
	public static boolean hoverItem(final Item item) {
		return hoverItem(item, Random.nextInt(1000, 2000));
	}

	/**
	 * Hovers the given item for a specified amount of time in milliseconds.
	 * 
	 * @param item
	 *            The item to hover.
	 * @param time
	 *            The minimum amount of time in milliseconds to hover the item.
	 * @return <tt>true</tt> if the item was hovered.
	 */
	public static boolean hoverItem(final Item item, final int time) {
		if (item == null) {
			return false;
		}
		if (item.getComponent() == null || !item.isComponentValid()) {
			return false;
		}
		item.getComponent().hover();
		Task.sleep(time);
		return true;
	}

	/**
	 * Right clicks the given item and hovers the specified menu action.
	 * 
	 * @param item
	 *            The item to right click.
	 * @param action
	 *            The action to hover.
	 * @return <tt>true</tt> if the item was right clicked and the menu action
	 *         was hovered.
	 */
	public static boolean hoverItem(final Item item, final String action) {
		if (!hoverItem(item, 0)) {
			return false;
		}
		return ExMenu.hoverAction(action);
	}
}
