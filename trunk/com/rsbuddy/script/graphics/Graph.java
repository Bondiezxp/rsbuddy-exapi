package com.rsbuddy.script.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedHashMap;

/**
 * @author Ramus
 * @author b0xb0x
 */
public class Graph {

	public static final Font DEFAULT_SCALE_FONT = new Font("Tahoma", Font.PLAIN, 9);
	public static final Font DEFAULT_LABEL_FONT = new Font("Tahoma", Font.PLAIN, 10);
	public static final Font DEFAULT_TITLE_FONT = new Font("Tahoma", Font.BOLD, 11);
	private final LinkedHashMap<Integer, Integer> yPoints = new LinkedHashMap<Integer, Integer>();
	private int yMin = -1;
	private int yMax = -1;
	private int added = 0;
	private final long startTime;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final int interval;
	private final Color axis;
	private final Color grid;
	private final Color points;
	private final Color labels;
	private final Color titleColor;
	private final Color scales;
	private final Font labelFont;
	private final Font titleFont;
	private final Font scaleFont;
	private final String xLabel;
	private final String yLabel;
	private final String title;

	public Graph(final int x, final int y, final int width, final int height, final Color axis, final Color grid,
			final Color points) {
		this(x, y, width, height, 10, axis, grid, points, Color.BLACK, Color.BLACK, Color.BLACK, DEFAULT_LABEL_FONT,
				DEFAULT_TITLE_FONT, DEFAULT_SCALE_FONT, "", "", "");
	}

	public Graph(final int x, final int y, final int width, final int height, final int interval, final Color axis,
			final Color grid, final Color points, final Color labels, final Color titleColor, final Color scales,
			final Font labelFont, final Font titleFont, final Font scaleFont, final String xLabel, final String yLabel,
			final String title) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.interval = interval;
		this.axis = axis;
		this.grid = grid;
		this.points = points;
		this.labels = labels;
		this.titleColor = titleColor;
		this.scales = scales;
		this.labelFont = labelFont;
		this.titleFont = titleFont;
		this.scaleFont = scaleFont;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.title = title;
		startTime = System.currentTimeMillis();
	}

	private void addPoint(final int yVal) {
		if ((System.currentTimeMillis() - startTime) / (width * 10) >= added) {
			yPoints.put(yVal, (int) (startTime - System.currentTimeMillis()));
			added += 1;
		}
	}

	private void drawAxis(final Graphics g) {
		g.setColor(grid);
		g.drawLine(x + width, y, x + width, y + height);
		g.setColor(axis);
		g.drawLine(x, y, x, y + height);
		g.drawLine(x, y + height, x + width, y + height);
	}

	private void drawGrid(final Graphics g) {
		g.setColor(grid);
		final double xplot = x + width;
		final double yplot = y + height;
		for (int i = 0; i < xplot / interval; i += 1) {
			g.drawLine(x, Math.max(x, (int) (i * (yplot / (xplot / interval)))), (int) xplot,
					Math.max(x, (int) (i * (yplot / (xplot / interval)))));
		}
		for (int i = 0; i < yplot / interval; i += 1) {
			g.drawLine(Math.max(y, (int) (i * (xplot / (yplot / interval)))), x,
					Math.max(y, (int) (i * (xplot / (yplot / interval)))), (int) yplot);
		}
		g.drawLine(width, y, width, height);
	}

	private void drawLabels(final Graphics g) {
		g.setFont(labelFont);
		g.setColor(labels);
		final FontMetrics fm = g.getFontMetrics(labelFont);
		final int w1 = fm.stringWidth(xLabel);
		final int w2 = fm.stringWidth(yLabel);
		g.drawString(xLabel, x + width / 2 - w1 / 2, y + height + labelFont.getSize());
		final Graphics2D g2 = (Graphics2D) g;
		g2.rotate(3 * Math.PI / 2, x, y + height);
		g2.drawString(yLabel, y + height / 2 - w2 / 2, y + height - 5);
		g2.rotate(-3 * Math.PI / 2, x, y + height);
	}

	private void drawPoints(final Graphics g) {
		Point prev = null;
		double offset = 0;
		g.setColor(points);
		for (final int y : yPoints.keySet()) {
			if (y < yMin || yMin == -1) {
				yMin = y;
			}
			if (y > yMax || yMax == -1) {
				yMax = y;
			}
			int ytmp = y;
			ytmp -= this.y;
			double yP = Math.abs(height * ((double) ytmp / (double) yMax) - height);
			yP += this.y;
			final Point p = new Point(Math.min(
					x + width,
					Math.max(x, (int) (x + width + offset - (System.currentTimeMillis() - startTime)
							/ (double) (x + width * interval)))), (int) yP);
			offset += width / (interval * 2);
			if (prev != null) {
				if (prev.x <= width + x && p.x <= width + x && prev.x >= x && p.x >= x) {
					g.drawLine(prev.x, prev.y, p.x, p.y);
				}
			}
			prev = p;
		}
	}

	private void drawScales(final Graphics g) {
		final FontMetrics fm = g.getFontMetrics(scaleFont);
		g.setFont(scaleFont);
		g.setColor(scales);
		final String min = Format.shortFormat(yMin);
		final String max = Format.shortFormat(yMax);
		final int w1 = fm.stringWidth(min);
		final int w2 = fm.stringWidth(max);
		g.drawString(min, x - w1 - 1, y + height + fm.getHeight() / 2);
		g.drawString(max, x - w2 - 1, y + fm.getHeight() / 2);
	}

	private void drawTitle(final Graphics g) {
		final FontMetrics fm = g.getFontMetrics(titleFont);
		final int w = fm.stringWidth(title);
		final int h = fm.getMaxAscent();
		g.setFont(titleFont);
		g.setColor(titleColor);
		g.drawString(title, x + 2 + width / 2 - w / 2, y - h / 2);
	}

	public void onRepaint(final Graphics g, final int average) {
		addPoint(average);
		drawGrid(g);
		drawPoints(g);
		drawAxis(g);
		drawScales(g);
		drawLabels(g);
		drawTitle(g);
	}
}