package com.rsbuddy.script.methods;

import com.rsbuddy.script.action.Action;
import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Condition;
import com.rsbuddy.script.util.Random;

import java.awt.Point;

/**
 * @author Ramus
 */
public class ExMenu {

	/**
	 * Hovers the action in the menu. This will open the menu where the current
	 * mouse position is.
	 * 
	 * @param action
	 *            The action to hover.
	 * @return <tt>true</tt> if the action was hovered.
	 */
	public static boolean hoverAction(final String action) {
		return hoverAction(action, 0);
	}

	/**
	 * Hovers the action in the menu. This will open the menu where the current
	 * mouse position is.
	 * 
	 * @param action
	 *            The action to hover.
	 * @param time
	 *            The minimum amount of time in milliseconds to hover the
	 *            action.
	 * @return <tt>true</tt> if the action was hovered.
	 */
	public static boolean hoverAction(final String action, final int time) {
		if (!Menu.isOpen()) {
			Mouse.click(false);
			Action.sleep(500, new Condition() {

				@Override
				public boolean isValid() {
					return Menu.isOpen();
				}
			});
		}
		if (!Menu.isOpen() || !Menu.contains(action) || Menu.isCollapsed()) {
			return false;
		}
		final Point menu = Menu.getLocation();
		if (!ExCalculations.isPointOnScreen(menu)) {
			return false;
		}
		final String[] items = Menu.getItems();
		final int idx = Menu.getIndex(action);
		if (idx > items.length) {
			return false;
		}
		final int xOff = Random.nextInt(4, items[idx].length() * 4);
		final int yOff = 21 + idx * 16 + Random.nextInt(0, 16);
		final Point p = new Point(menu.x + xOff, menu.y + yOff);
		if (!ExCalculations.isPointOnScreen(p) || !Menu.isOpen()) {
			return false;
		}
		Mouse.move(p);
		if (!Menu.isOpen()) {
			return false;
		}
		Task.sleep(time);
		return Menu.isOpen() && Menu.contains(action);
	}
}