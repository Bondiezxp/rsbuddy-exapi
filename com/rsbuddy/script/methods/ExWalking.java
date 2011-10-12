package com.rsbuddy.script.methods;

import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.LocalPath;
import com.rsbuddy.script.wrappers.Path.TraversalOption;
import com.rsbuddy.script.wrappers.Tile;
import com.rsbuddy.script.wrappers.TilePath;

import java.util.EnumSet;

/**
 * @author Ramus
 */
public class ExWalking {

	/**
	 * Gets the closest tile to the specified tile.
	 * 
	 * @param tile
	 *            The tile to get a closer tile to.
	 * @return A tile close to the specified tile.
	 */
	public static Tile getClosest(final Tile tile) {
		final LocalPath localPath = (LocalPath) Walking.findPath(tile);
		if (localPath.getNext() == null) {
			return tile;
		}
		final TilePath tilePath = localPath.getCurrentTilePath();
		if (tilePath.getNext() == null) {
			return tile;
		}
		final Tile[] tiles = tilePath.toArray();
		final Tile next = getNext(tiles);
		if (next == null) {
			return tile;
		}
		return next;
	}

	/**
	 * Gets the next tile in a tile array.
	 * 
	 * @param tiles
	 *            The tile array to get the next tile from.
	 * @return The next tile from the tile array.
	 */
	public static Tile getNext(final Tile[] tiles) {
		for (int i = tiles.length - 1; i > 0; i -= 1) {
			if (tiles[i].isOnMap()) {
				return tiles[i];
			}
		}
		return null;
	}

	/**
	 * Gets a random tile from the specified area.
	 * 
	 * @param area
	 *            The area to get a random tile from.
	 * @return A random tile in the area.
	 */
	public static Tile getRandom(final Area area) {
		final Tile[] tiles = area.getTileArray();
		return tiles[Random.nextInt(0, tiles.length)];
	}

	/**
	 * Reverses the specified tile array.
	 * 
	 * @param tiles
	 *            Tile tile array to reverse;
	 * @return The reversed tile array.
	 */
	public static Tile[] reverse(final Tile[] tiles) {
		final Tile[] rev = new Tile[tiles.length];
		for (int i = tiles.length - 1; i > 0; i -= 1) {
			rev[tiles.length - i - 1] = tiles[i];
		}
		return rev;
	}

	/**
	 * Walks the specified tile array.
	 * 
	 * @param tiles
	 *            The tile array to be walked.
	 * @return <tt>true</tt> if the end tile was reached; <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean walk(final Tile[] tiles) {
		final Tile next = getNext(tiles);
		if (next == null) {
			return false;
		}
		return Walking.findPath(next).traverse(EnumSet.of(TraversalOption.HANDLE_RUN, TraversalOption.SPACE_ACTIONS));
	}

	/**
	 * Walks to the specified area. This handles running.
	 * 
	 * @param area
	 *            The area to walk to.
	 * @return <tt>true</tt> if the specified area was reached; <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean walkTo(final Area area) {
		final Tile dest;
		if ((dest = Walking.getDestination()) != null && area.contains(dest) && Players.getLocal().isMoving()) {
			return true;
		}
		return walkTo(getRandom(area));
	}

	/**
	 * Walks to the specified tile. This handles running.
	 * 
	 * @param tile
	 *            The tile to walk to.
	 * @return <tt>true</tt> if the specified tile was reached; <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean walkTo(final Tile tile) {
		final LocalPath localPath = (LocalPath) Walking.findPath(tile);
		if (localPath == null || localPath.getNext() == null) {
			return false;
		}
		final TilePath tilePath = localPath.getCurrentTilePath();
		if (tilePath == null || tilePath.getNext() == null) {
			return false;
		}
		final Tile[] tiles = tilePath.toArray();
		if (tiles == null) {
			return false;
		}
		final Tile next = getNext(tiles);
		if (next == null) {
			return false;
		}
		return Walking.findPath(next).traverse(EnumSet.of(TraversalOption.HANDLE_RUN, TraversalOption.SPACE_ACTIONS));
	}
}