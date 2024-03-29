package com.rsbuddy.script.util;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.task.LoopTask;
import com.rsbuddy.script.wrappers.Item;

import org.rsbuddy.tabs.Inventory;

public class InventoryTracker extends LoopTask {

	private final int[] itemIds;
	private int lastCount = -1;
	private int count = 0;
	private final boolean cache;

	public InventoryTracker(final boolean cache, final int... itemIds) {
		this.itemIds = itemIds;
		this.cache = cache;
	}

	public InventoryTracker(final int... itemIds) {
		this(false, itemIds);
	}

	/**
	 * Gets the amount of items (that match the given ids) that have been gained
	 * since this has been started.
	 * 
	 * @return The amount of items (that match the given ids) that have been
	 *         gained since this has been started.
	 */
	public int getAmountGained() {
		return count;
	}

	/**
	 * Gets the current item count in the inventory.
	 * 
	 * @return The amount of items that match one of the itemIds.
	 */
	private int getCount() {
		int count = 0;
		final Item[] items = cache ? Inventory.getCachedItems() : Inventory.getItems();
		for (final Item item : items) {
			if (item == null) {
				continue;
			}
			for (final int id : itemIds) {
				if (item.getId() == id) {
					count += 1;
				}
			}
		}
		return count;
	}

	/**
	 * Gets the current amount of items (that match the given ids) in the
	 * inventory.
	 * 
	 * @return The current amount of items (that match the given ids) in the
	 *         inventory.
	 */
	public int getCurrentAmount() {
		return lastCount = getCount();
	}

	@Override
	public int loop() {
		if (!Game.isLoggedIn() || !isActive()) {
			return 0;
		}
		if (lastCount < 0) {
			lastCount = getCount();
			return 0;
		} else if (getCount() == 0) {
			lastCount = 0;
			return 0;
		} else if (getCount() > lastCount) {
			count += getCount() - lastCount;
			lastCount = getCount();
			return 0;
		}
		return 0;
	}
}