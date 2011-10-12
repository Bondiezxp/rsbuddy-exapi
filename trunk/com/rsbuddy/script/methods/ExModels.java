package com.rsbuddy.script.methods;

import com.rsbuddy.script.wrappers.Model;

import java.awt.Graphics;
import java.awt.Polygon;

/**
 * @author Ramus
 */
public class ExModels {

	/**
	 * Draws the specified model on the screen.
	 * 
	 * @param g
	 *            The graphics to draw the model with.
	 * @param model
	 *            The model to draw.
	 */
	public static void draw(final Graphics g, final Model model) {
		if (model == null || model.getTriangles() == null || model.getTriangles().length < 1) {
			return;
		}
		for (final Polygon p : model.getTriangles()) {
			g.drawPolygon(p);
		}
	}

	/**
	 * Fills the specified model on the screen.
	 * 
	 * @param g
	 *            The graphics to fill the model with.
	 * @param model
	 *            The model to fill.
	 */
	public static void fill(final Graphics g, final Model model) {
		if (model == null || model.getTriangles() == null || model.getTriangles().length < 1) {
			return;
		}
		for (final Polygon p : model.getTriangles()) {
			g.fillPolygon(p);
		}
	}
}
