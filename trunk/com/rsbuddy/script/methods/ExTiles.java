package com.rsbuddy.script.methods;

import com.rsbuddy.script.wrappers.LocalPath;
import com.rsbuddy.script.wrappers.Tile;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;

public class ExTiles {

	/**
	 * Draws the tile(s) on the minimap.
	 * 
	 * @param g
	 *            The graphics.
	 * @param tiles
	 *            The tile(s).
	 */
	public static void drawOnMap(final Graphics g, final Tile... tiles) {
		for (final Tile tile : tiles) {
			if (tile == null || !tile.isOnMap()) {
				return;
			}
			final Point p = tile.toMap();
			g.drawRect((int) (p.x - 1.5), (int) (p.y - 1.5), 3, 3);
		}
	}

	/**
	 * Draws the tile(s) on the screen.
	 * 
	 * @param g
	 *            The graphics.
	 * @param tiles
	 *            The tile(s).
	 */
	public static void drawOnScreen(final Graphics g, final Tile... tiles) {
		for (final Tile tile : tiles) {
			if (tile == null || !tile.isOnScreen()) {
				return;
			}
			final Point bl = tile.getPoint(0, 0, 0);
			final Point br = tile.getPoint(1, 0, 0);
			final Point tr = tile.getPoint(1, 1, 0);
			final Point tl = tile.getPoint(0, 1, 0);
			if (!ExCalculations.isPointOnScreen(new Point[] { bl, br, tr, tl })) {
				return;
			}
			g.drawPolygon(new Polygon(new int[] { bl.x, br.x, tr.x, tl.x },
					new int[] { bl.y, br.y, tr.y, tl.y }, 4));
		}
	}

	/**
	 * Fills the tile(s) on the minimap.
	 * 
	 * @param g
	 *            The graphics.
	 * @param tiles
	 *            The tile(s).
	 */
	public static void fillOnMap(final Graphics g, final Tile... tiles) {
		for (final Tile tile : tiles) {
			if (tile == null || !tile.isOnMap()) {
				return;
			}
			final Point p = tile.toMap();
			g.fillRect((int) (p.x - 1.5), (int) (p.y - 1.5), 3, 3);
		}
	}

	/**
	 * Fills the tile(s) on the screen.
	 * 
	 * @param g
	 *            The graphics.
	 * @param tiles
	 *            The tile(s).
	 */
	public static void fillOnScreen(final Graphics g, final Tile... tiles) {
		for (final Tile tile : tiles) {
			if (tile == null || !tile.isOnScreen()) {
				return;
			}
			final Point bl = tile.getPoint(0, 0, 0);
			final Point br = tile.getPoint(1, 0, 0);
			final Point tr = tile.getPoint(1, 1, 0);
			final Point tl = tile.getPoint(0, 1, 0);
			if (!ExCalculations.isPointOnScreen(new Point[] { bl, br, tr, tl })) {
				return;
			}
			final Polygon poly = new Polygon(
					new int[] { bl.x, br.x, tr.x, tl.x }, new int[] { bl.y,
							br.y, tr.y, tl.y }, 4);
			g.fillPolygon(poly);
		}
	}

	/**
	 * Gets all the adjacent tiles to the specified tile. Diagonal tiles are
	 * optional.
	 * 
	 * @param tile
	 *            The tile to get the adjacent tiles of.
	 * @param diagonal
	 *            <tt>true</tt> to get all eight tiles; <tt>false</tt> for only
	 *            4 (N, E, S, W).
	 * @return The adjacent tiles of the specified tile.
	 */
	public static Tile[] getAdjacentTilesTo(final Tile tile,
			final boolean diagonal) {
		if (tile == null) {
			return null;
		}
		final LinkedList<Tile> tiles = new LinkedList<Tile>();
		final int x = tile.getX();
		final int y = tile.getY();
		tiles.add(new Tile(x - 1, y));
		tiles.add(new Tile(x, y - 1));
		tiles.add(new Tile(x + 1, y));
		tiles.add(new Tile(x, y + 1));
		if (diagonal) {
			tiles.add(new Tile(x - 1, y - 1));
			tiles.add(new Tile(x + 1, y + 1));
			tiles.add(new Tile(x - 1, y + 1));
			tiles.add(new Tile(x + 1, y - 1));
		}
		return tiles.toArray(new Tile[tiles.size()]);
	}

	/**
	 * Gets the closest diagonal tile between the player and the tile that is
	 * visible on the minimap.
	 * 
	 * @param tile
	 *            The tile.
	 * @return The closest diagonal tile between the player and the tile that is
	 *         visible on the minimap.
	 */
	public static Tile getClosest(final Tile tile) {
		final Tile me = Players.getLocal().getLocation();
		final Tile half = new Tile((me.getX() + tile.getLocation().getX()) / 2,
				(me.getY() + tile.getLocation().getY()) / 2);
		if (!half.isOnMap()) {
			return getClosest(half);
		}
		return half;
	}

	/**
	 * Gets all tiles on the minimap.
	 * 
	 * @return The minimap tiles.
	 */
	public static Tile[] getMinimap() {
		final LinkedList<Tile> tiles = new LinkedList<Tile>();
		for (int x = 0; x < 105; x += 1) {
			for (int y = 0; y < 105; y += 1) {
				final Tile tile = new Tile(x + Game.getMapBase().getX(), y
						+ Game.getMapBase().getY());
				if (!tile.isOnMap()) {
					continue;
				}
				tiles.add(tile);
			}
		}
		return tiles.toArray(new Tile[tiles.size()]);
	}

	/**
	 * Gets a tile under the specified point.
	 * 
	 * @param p
	 *            The point to get the tile at.
	 * @param minimap
	 *            <tt>true</tt> if the point is on the minimap; <tt>false</tt>
	 *            if it is on screen.
	 * @return The nearest tile to the point.
	 */
	public static Tile getUnderPoint(final Point p, final boolean minimap) {
		if (!Menu.contains("Walk here")) {
			return null;
		}
		Tile close = null;
		for (int x = 0; x < 105; x += 1) {
			for (int y = 0; y < 105; y += 1) {
				final Tile tile = new Tile(x + Game.getMapBase().getX(), y
						+ Game.getMapBase().getY());
				if (tile == null
						|| (minimap ? !tile.isOnMap() : !tile.isOnScreen())) {
					continue;
				}
				final Point s = minimap ? tile.toMap() : tile.getCenterPoint();
				if (s.x == -1 || s.y == -1 || s == null) {
					continue;
				}
				if (close == null
						|| (minimap ? close.toMap().distance(p) > tile.toMap()
								.distance(p) : close.getCenterPoint().distance(
								p) > tile.getCenterPoint().distance(p))) {
					close = tile;
				}
			}
		}
		return close;
	}

	/**
	 * Author - Biking. Checks wether or not the specified tile is blocked.
	 * 
	 * @param tile
	 *            The tile to check.
	 * @return <tt>true</tt> if the tile is blocked; <tt>false</tt> otherwise.
	 */
	public static boolean isBlocked(final Tile tile) {
		final int[][] flags = Walking.getCollisionFlags(Game.getFloorLevel());
		final Tile offset = Walking.getCollisionOffset(Game.getFloorLevel());
		final Tile baseTile = Game.getMapBase();
		if ((flags[tile.getX() - (baseTile.getX() + offset.getX())][tile.getY()
				- (baseTile.getY() + offset.getY())] & LocalPath.BLOCKED) != 0) {
			return true;
		}
		return false;
	}
}
