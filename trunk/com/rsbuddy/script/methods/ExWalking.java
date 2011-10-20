package com.rsbuddy.script.methods;

import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.LocalPath;
import com.rsbuddy.script.wrappers.Tile;
import com.rsbuddy.script.wrappers.TilePath;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.tabs.Magic;
import org.rsbuddy.tabs.Magic.Book;
import org.rsbuddy.tabs.Magic.Spell;
import org.rsbuddy.tabs.Quest;

/**
 * @author Ramus
 */
@SuppressWarnings("unused")
public class ExWalking {

	public static enum Ferry {

		KARAMJA("Karamja", 30, null, null, null);

		private final String name;
		private final int cost;
		private final Area port;
		private final Tile location;
		private final String quest;

		private Ferry(final String name, final int cost, final String quest, final Area port, final Tile location) {
			this.name = name;
			this.cost = cost;
			this.quest = quest;
			this.port = port;
			this.location = location;
		}

		public int getCost() {
			return cost;
		}

		public Tile getLocation() {
			return location;
		}

		public String getName() {
			return name;
		}

		public Area getPort() {
			return port;
		}

		public String getQuest() {
			return quest;
		}
	}

	public static enum Teleport {

		LUMBRIDGE("Lumbridge", Magic.Modern.LUMBRIDGE_TELEPORT, Book.MODERN, null, null);

		private final String name;
		private final Spell spell;
		private final Area location;

		private Teleport(final String name, final Spell spell, final Book book, final String quest, final Area location) {
			this.name = name;
			this.spell = spell;
			this.location = location;
		}

		public Area getLocation() {
			return location;
		}

		public String getName() {
			return name;
		}

		public Spell getSpell() {
			return spell;
		}
	}

	private static final String[][] OBSTACLES = { { "door", "open" }, { "gate", "open" }, { "stile", "climb-over" } };
	private static final String[][] PLANE_OBSTACLES = { { "ladder", "climb-up", "climb-down" },
			{ "stairs", "climb-up", "climb-down" } };
	private static String action = null;

	private static boolean canTakeFerry(final Ferry ferry) {
		return ferry != null && (ferry.getQuest() == null || Quest.isCompleted(ferry.getQuest()));
	}

	private static boolean canTeleport(final Teleport tele) {
		return tele != null && Skills.getRealLevel(Skills.MAGIC) < tele.getSpell().getLevel();
	}

	private static Ferry getBestFerry(final Tile dest) {
		double dist = Calculations.distanceTo(dest);
		Ferry ferry = null;
		for (final Ferry f : Ferry.values()) {
			final double tmp_dist;
			if ((tmp_dist = Calculations.distanceBetween(dest, f.getLocation())) < dist) {
				dist = tmp_dist;
				ferry = f;
			}
		}
		return ferry;
	}

	private static Teleport getBestTeleport(final Tile dest) {
		Teleport tele = null;
		double dist = Calculations.distanceTo(dest);
		for (final Teleport t : Teleport.values()) {
			final double tmp_dist;
			if ((tmp_dist = Calculations.distanceBetween(dest, t.getLocation().getCentralTile())) < dist) {
				dist = tmp_dist;
				tele = t;
			}
		}
		return tele;
	}

	/**
	 * Gets the closest tile to the specified tile.
	 * 
	 * @param tile
	 *            The tile to get a closer tile to.
	 * @return A tile close to the specified tile.
	 */
	public static Tile getClosest(final Tile tile) {
		final LocalPath localPath = (LocalPath) Walking.findPath(tile);
		if (localPath == null || localPath.getNext() == null) {
			return Walking.getTileOnMap(tile);
		}
		final TilePath tilePath = localPath.getCurrentTilePath();
		if (tilePath.getNext() == null) {
			return Walking.getTileOnMap(tile);
		}
		final Tile[] tiles = tilePath.toArray();
		final Tile next = getNext(tiles);
		if (next == null) {
			return Walking.getTileOnMap(tile);
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

	private static boolean hasCoinsForFerry(final Ferry ferry) {
		return ferry != null && Inventory.getCount(995) >= ferry.getCost();
	}

	private static boolean hasItemsToTeleport(final Teleport tele) {
		if (tele == null) {
			return false;
		}
		for (int i = 0; i < tele.getSpell().getRunes().length; i += 1) {
			final int ids[] = tele.getSpell().getRunes()[i];
			final int amount = tele.getSpell().getAmounts()[i];
			if (Inventory.getCount(ids) < amount) {
				return false;
			}
		}
		return true;
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

	private static boolean sail(final Ferry ferry) {
		final Tile dest = ferry.getLocation();
		if (dest == null) {
			return false;
		}
		if (!dest.isOnScreen()) {
			final Teleport tele = getBestTeleport(dest);
			if (tele != null) {
				return teleport(tele);
			}
			return traverse(dest);
		}
		if (!hasCoinsForFerry(ferry)) {
			// TODO Bank for coins..
			return false;
		}
		return false; // TODO Sail...
	}

	private static boolean teleport(final Teleport tele) {
		if (!hasItemsToTeleport(tele)) {
			// TODO Bank for items..
			return false;
		}
		if (!Magic.castSpell(tele.getSpell())) {
			return false;
		}
		for (int i = 0; !tele.getLocation().contains(Players.getLocal().getLocation()) && i < 10; i += 1) {
			Task.sleep(250);
		}
		return tele.getLocation().contains(Players.getLocal().getLocation());
	}

	private static boolean traverse(final Tile dest) {
		final LocalPath localPath = (LocalPath) Walking.findPath(dest);
		if (localPath == null) {
			return false;
		}
		if (localPath.isValid()) {
			if (localPath.getEnd().isOnScreen()) {
				return localPath.getEnd().interact("Walk here");
			} else if (localPath.getNext().isOnScreen()) {
				return localPath.getEnd().interact("Walk here");
			}
			return localPath.traverse();
		}
		if (!traverseDoor(dest, localPath)) {
			return false;
		}
		return traverse(dest);
	}

	private static boolean traverseDoor(final Tile dest, final LocalPath localPath) {
		final GameObject block = Objects.getNearest(new Filter<GameObject>() {

			@Override
			public boolean accept(final GameObject go) {
				if (go == null || go.getDef() == null || go.getDef().getActions() == null
						|| go.getDef().getName() == null) {
					return false;
				}
				boolean found = false;
				int i;
				for (i = 0; i < OBSTACLES.length; i += 1) {
					if (go.getDef().getName().equalsIgnoreCase(OBSTACLES[i][0])) {
						found = true;
						break;
					}
				}
				if (found) {
					return false;
				}
				for (int j = 0; j < OBSTACLES[i].length; j += 1) {
					for (final String act : go.getDef().getActions()) {
						if (OBSTACLES[i][j].equalsIgnoreCase(act)) {
							ExWalking.action = act;
							return true;
						}
					}
				}
				return false;
			}
		});
		if (Calculations.distanceTo(dest) < Calculations.distanceTo(block)) {
			return traverse(dest);
		}
		if (block == null || action == null) {
			return true;
		}
		if (!block.isOnScreen()) {
			return block.getLocation().clickOnMap();
		}
		if (!block.interact(action)) {
			return false;
		}
		for (int i = 0; block != null && i < 10; i += 1) {
			Task.sleep(250);
		}
		return true;
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
		return walkTo(next);
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
	 * @param dest
	 *            The tile to walk to.
	 * @return <tt>true</tt> if the specified tile was reached; <tt>false</tt>
	 *         otherwise.
	 */
	public static boolean walkTo(final Tile dest) {
		// final Ferry ferry = getBestFerry(dest);
		// if (ferry != null && canTakeFerry(ferry)) {
		// return sail(ferry);
		// }
		// final Teleport tele = getBestTeleport(dest);
		// if (tele != null && canTeleport(tele)) {
		// return teleport(tele);
		// }
		final LocalPath localPath = (LocalPath) Walking.findPath(dest);
		if (localPath == null || localPath.getNext() == null) {
			return traverse(dest);
		}
		final TilePath tilePath = localPath.getCurrentTilePath();
		if (tilePath == null) {
			return traverse(dest);
		}
		final Tile[] tiles = tilePath.toArray();
		if (tiles == null) {
			return traverse(dest);
		}
		final Tile next = getNext(tiles);
		if (next == null) {
			return traverse(dest);
		}
		return traverse(next);
	}
}