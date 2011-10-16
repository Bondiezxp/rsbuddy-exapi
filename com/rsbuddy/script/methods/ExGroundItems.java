package com.rsbuddy.script.methods;

import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.GameObject.Type;
import com.rsbuddy.script.wrappers.GroundItem;

import java.awt.Point;
import java.awt.Polygon;

/**
 * @author Ramus
 */
public class ExGroundItems {

	/**
	 * Returns wether or not the ground item is at a boundary.
	 * 
	 * @param item
	 *            The ground item to check.
	 * @return <tt>true</tt> if something intersects the ground item;
	 *         <tt>false</tt> otherwise.
	 */
	public static boolean atBoundary(final GroundItem item) {
		if (!isValid(item) || !item.getLocation().isOnScreen()) {
			return false;
		}
		return intersects(item);
	}

	/**
	 * Returns wether or not the specified ground item intersects with any
	 * objects.
	 * 
	 * @param item
	 *            The ground item to check.
	 * @return <tt>true</tt> if no objects intersects the given ground item.
	 */
	public static boolean intersects(final GroundItem item) {
		if (!isValid(item)) {
			return false;
		}
		for (final GameObject go : Objects.getLoaded()) {
			if (go == null || go.getModel() == null || go.getLocation() == null) {
				continue;
			}
			if (!ExCalculations.isPointOnScreen(go.getLocation().getCenterPoint())) {
				continue;
			}
			if (go.getType() != Type.BOUNDARY || go.getType() != GameObject.Type.BOUNDARY) {
				continue;
			}
			for (final Polygon p1 : go.getModel().getTriangles()) {
				final Point p = item.getLocation().getPoint(0.5, 0.5, 0);
				if (p1.getBounds().contains(p)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks wether the specified ground item is valid.
	 * 
	 * @param item
	 *            The ground item to check.
	 * @return <tt>true</tt> if the ground item is valid; <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean isValid(final GroundItem item) {
		if (item == null || item.getItem() == null || !item.getItem().isComponentValid()
				|| item.getItem().getId() == -1 || item.getLocation() == null) {
			return false;
		}
		for (final GroundItem gi : GroundItems.getAllAt(item.getLocation())) {
			if (gi == null || gi.getItem() == null || gi.getItem().getId() == -1 || gi.getLocation() == null) {
				return false;
			}
			if (gi.getItem().getId() == item.getItem().getId()) {
				return true;
			}
		}
		return false;
	}
}
