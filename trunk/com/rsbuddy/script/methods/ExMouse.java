package com.rsbuddy.script.methods;

import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Locatable;
import com.rsbuddy.script.wrappers.Model;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.LinkedList;

public class ExMouse {

	private static class MouseClickPoint extends Point {

		private static final long serialVersionUID = 3668956253646817526L;
		private final long clickTime;
		private final long finishTime;
		private final double lastingTime = 2500;

		public MouseClickPoint(final int x, final int y, final long clickTime) {
			super(x, y);
			this.x = x;
			this.y = y;
			this.clickTime = clickTime;
			finishTime = clickTime + (int) lastingTime;
		}

		public boolean equals(final long time) {
			return clickTime == time;
		}

		public Color getColor(final Color c) {
			return ExCalculations.Paint
					.setAlpha(c, toColor(256.0D * ((finishTime - System
							.currentTimeMillis()) / lastingTime)));
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}
	}

	private static class MousePathPoint extends Point {

		private static final long serialVersionUID = -7943088685545541809L;
		private final double lastingTime = 2500;
		private final long finishTime;

		public MousePathPoint(final int x, final int y) {
			super(x, y);
			finishTime = System.currentTimeMillis() + (int) lastingTime;
		}

		public Color getColor(final Color c) {
			return ExCalculations.Paint
					.setAlpha(c, toColor(256.0D * ((finishTime - System
							.currentTimeMillis()) / lastingTime)));
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}
	}

	private static final LinkedList<MousePathPoint> MOUSE_PATH = new LinkedList<MousePathPoint>();
	private static final LinkedList<MouseClickPoint> MOUSE_CLICKS = new LinkedList<MouseClickPoint>();

	/**
	 * Draws a simple rotating mouse.
	 * 
	 * @param g1
	 *            The graphics.
	 * @param inner
	 *            The color of the inner rotation.
	 * @param outer
	 *            The color of the outer rotation.
	 * @param mouseTrail
	 *            The color of the mouse trail.
	 * @param mouseClicks
	 *            The color of the mouse clicks.
	 */
	public static void drawMouse(final Graphics2D g1, final Color inner,
			final Color outer, final Color mouseTrail, final Color mouseClicks) {
		final Point point = Mouse.getLocation();
		final int x = point.x;
		final int y = point.y;
		final Graphics2D gr = (Graphics2D) g1.create();
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gr.rotate(Math.toRadians(getRotation(5)), x, y);
		gr.setColor(inner);
		gr.drawRect(x - 4, y - 4, 8, 8);
		final Graphics2D grr = (Graphics2D) g1.create();
		grr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		grr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		grr.rotate(Math.toRadians(getRotation(-5)), x, y);
		grr.setColor(outer);
		grr.drawRect(x - 8, y - 8, 16, 16);
		final Graphics2D g = (Graphics2D) g1.create();
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		while (!MOUSE_PATH.isEmpty() && MOUSE_PATH.peek().isUp()) {
			MOUSE_PATH.remove();
		}
		final MousePathPoint mpp = new MousePathPoint(point.x, point.y);
		if (MOUSE_PATH.isEmpty() || !MOUSE_PATH.getLast().equals(mpp)) {
			MOUSE_PATH.add(mpp);
		}
		MousePathPoint lastPoint = null;
		for (final MousePathPoint p : MOUSE_PATH) {
			if (p.x < 0 || p.y < 0 || p.x > Game.getCanvasSize().width
					|| p.y > Game.getCanvasSize().width) {
				MOUSE_PATH.remove(p);
			}
			if (lastPoint != null && lastPoint.x > 0 && lastPoint.y > 0
					&& lastPoint.x < Game.getCanvasSize().width
					&& lastPoint.y < Game.getCanvasSize().height) {
				g.setColor(p.getColor(mouseTrail));
				g.drawLine(p.x, p.y, lastPoint.x, lastPoint.y);
			}
			lastPoint = p;
		}
		while (!MOUSE_CLICKS.isEmpty() && MOUSE_CLICKS.peek().isUp()) {
			MOUSE_CLICKS.remove();
		}
		final long clickTime = Mouse.getPressTime();
		final Point lastClickPos = Mouse.getPressLocation();
		if (MOUSE_CLICKS.isEmpty() || !MOUSE_CLICKS.getLast().equals(clickTime)) {
			MOUSE_CLICKS.add(new MouseClickPoint(lastClickPos.x,
					lastClickPos.y, clickTime));
		}
		for (final MouseClickPoint c : MOUSE_CLICKS) {
			g.setColor(c.getColor(mouseClicks));
			g.fillOval(c.x - 2, c.y - 2, 4, 4);
		}
	}

	/**
	 * Author - Enfilade. Generates a random distance between minDistance and
	 * maxDistance from the current position of the mouse by generating random
	 * vector and then multiplying it by a random number between minDistance and
	 * maxDistance. The maximum distance is cut short if the mouse would go off
	 * screen in the direction it chose.
	 * 
	 * @param minDistance
	 *            The minimum distance the cursor will move
	 * @param maxDistance
	 *            The maximum distance the cursor will move (exclusive)
	 * @return A random point between minDistance and maxDistance.
	 */
	private static Point generateRandomPoint(final int minDistance,
			final int maxDistance) {
		double xvec = Math.random();
		if (Random.nextInt(0, 2) == 1) {
			xvec = -xvec;
		}
		double yvec = Math.sqrt(1 - xvec * xvec);
		if (Random.nextInt(0, 2) == 1) {
			yvec = -yvec;
		}
		double distance = maxDistance;
		final Point p = Mouse.getLocation();
		final int maxX = (int) Math.round(xvec * distance + p.x);
		distance -= Math.abs((maxX - Math.max(0,
				Math.min(Game.getCanvasSize().getWidth(), maxX)))
				/ xvec);
		final int maxY = (int) Math.round(yvec * distance + p.y);
		distance -= Math.abs((maxY - Math.max(0,
				Math.min(Game.getCanvasSize().getHeight(), maxY)))
				/ yvec);
		if (distance < minDistance) {
			return generateRandomPoint(maxDistance, minDistance);
		}
		distance = Random.nextInt(minDistance, (int) distance);
		return new Point((int) (xvec * distance) + p.x, (int) (yvec * distance)
				+ p.y);
	}

	/**
	 * Gets the angle rotation, used for rotating graphics.
	 * 
	 * @param ticks
	 * @return The angle rotation, used for rotating graphics.
	 */
	private static double getRotation(final int ticks) {
		return System.currentTimeMillis() % (360 * ticks) / ticks;
	}

	/**
	 * Hovers the specified game objects' action. This will open the menu if it
	 * has to.
	 * 
	 * @param go
	 *            The game object to hover.
	 * @param action
	 *            The action in the menu to hover.
	 * @return <tt>true</tt> if the action was hovered.
	 */
	public static boolean hover(final GameObject go, final String action) {
		if (go == null) {
			return false;
		}
		final Model model = go.getModel();
		if (model == null) {
			if (go.getLocation() == null) {
				return false;
			}
			return hover(go.getLocation(), action);
		}
		return hover(go.getModel(), action);
	}

	/**
	 * Hovers the specified models' action. This will open the menu if it has
	 * to.
	 * 
	 * @param model
	 *            The model to hover.
	 * @param action
	 *            The action in the menu to hover.
	 * @return <tt>true</tt> if the action was hovered.
	 */
	public static boolean hover(final Model model, final String action) {
		if (model == null) {
			return false;
		}
		Point p = ExCalculations.getRandomPoint(model);
		if (!ExCalculations.isPointOnScreen(p)) {
			return false;
		}
		final int speed = Mouse.getSpeed();
		Mouse.setSpeed(Random.nextInt(5, 10));
		for (int i = 0; i < 10 && model != null
				&& ExCalculations.isPointOnScreen(p); i += 1) {
			p = ExCalculations.getRandomPoint(model);
			if (!ExCalculations.isPointOnScreen(p)) {
				return false;
			}
			Mouse.move(p);
			if (!model.contains(p)) {
				continue;
			}
			if (action == null) {
				Mouse.setSpeed(speed);
				return true;
			}
			if (Menu.contains(action)) {
				Mouse.setSpeed(speed);
				return ExMenu.hoverAction(action);
			}
		}
		Mouse.setSpeed(speed);
		return false;
	}

	/**
	 * Hovers the specified tiles' action. This will open the menu if it has to.
	 * 
	 * @param tile
	 *            The tile to hover.
	 * @param action
	 *            The action in the menu to hover.
	 * @return <tt>true</tt> if the action was hovered.
	 */
	public static boolean hover(final Tile tile, final String action) {
		if (tile == null) {
			return false;
		}
		Point p = ExCalculations.getRandomPoint(tile);
		if (!ExCalculations.isPointOnScreen(p)) {
			return false;
		}
		final int speed = Mouse.getSpeed();
		Mouse.setSpeed(Random.nextInt(5, 10));
		for (int i = 0; i < 10 && tile != null && tile.isOnScreen(); i += 1) {
			p = ExCalculations.getRandomPoint(tile);
			if (!ExCalculations.isPointOnScreen(p)) {
				return false;
			}
			Mouse.move(p);
			if (!tile.contains(p)) {
				continue;
			}
			if (action == null) {
				Mouse.setSpeed(speed);
				return true;
			}
			if (Menu.contains(action)) {
				Mouse.setSpeed(speed);
				return ExMenu.hoverAction(action);
			}
		}
		Mouse.setSpeed(speed);
		return false;
	}

	/**
	 * Hovers the specified tiles' action. This will open the menu if it has to.
	 * 
	 * @param tile
	 *            The tile to hover.
	 * @param action
	 *            The action in the menu to hover.
	 * @param dX
	 *            The x value of were to click on the tile (0.0 - 1.0).
	 * @param dY
	 *            The y value of were to click on the tile (0.0 - 1.0).
	 * @param height
	 *            The height at which the tile is.
	 * @return <tt>true</tt> if the action was hovered.
	 */
	public static boolean hover(final Tile tile, final String action,
			final double dX, final double dY, final int height) {
		if (tile == null) {
			return false;
		}
		Point p = tile.getPoint(dX, dY, height);
		if (!ExCalculations.isPointOnScreen(p)) {
			return false;
		}
		final int speed = Mouse.getSpeed();
		Mouse.setSpeed(Random.nextInt(5, 10));
		for (int i = 0; i < 10 && tile != null && tile.isOnScreen(); i += 1) {
			p = tile.getPoint(dX + Random.nextDouble(-0.1, 0.1),
					dY + Random.nextDouble(-0.1, 0.1), height);
			if (!ExCalculations.isPointOnScreen(p)) {
				return false;
			}
			Mouse.move(p);
			if (!tile.contains(p)) {
				continue;
			}
			if (action == null) {
				Mouse.setSpeed(speed);
				return true;
			}
			if (Menu.contains(action)) {
				Mouse.setSpeed(speed);
				return ExMenu.hoverAction(action);
			}
		}
		Mouse.setSpeed(speed);
		return false;
	}

	/**
	 * Interacts with the specified game object.
	 * 
	 * @param go
	 *            The game object to interact with.
	 * @param action
	 *            The action to do.
	 * @return <tt>true</tt> if the game object was interacted with
	 *         successfully; <tt>false</tt> otherwise.
	 */
	public static boolean interact(final GameObject go, final String action) {
		if (go == null) {
			return false;
		}
		final Model model = go.getModel();
		if (model == null) {
			if (go.getLocation() == null) {
				return false;
			}
			return interact(go.getLocation(), action);
		}
		if (!interact(model, action)) {
			return go.interact(action);
		} else {
			return true;
		}
	}

	/**
	 * Interacts with the specified locatable.
	 * 
	 * @param loc
	 *            The locatable to interact with.
	 * @param action
	 *            The action to do.
	 * @return <tt>true</tt> if the locatable was interacted with successfully;
	 *         <tt>false</tt> otherwise.
	 */
	public static boolean interact(final Locatable loc, final String action) {
		if (loc == null) {
			return false;
		}
		if (loc.getLocation() == null) {
			return false;
		}
		if (!interact(loc.getLocation(), action)) {
			return loc.getLocation().interact(action);
		} else {
			return true;
		}
	}

	/**
	 * Interacts with the specified model.
	 * 
	 * @param model
	 *            The model to interact with.
	 * @param action
	 *            The action to do.
	 * @return <tt>true</tt> if the model was interacted with successfully;
	 *         <tt>false</tt> otherwise.
	 */
	public static boolean interact(final Model model, final String action) {
		if (model == null) {
			return false;
		}
		Point p = ExCalculations.getRandomPoint(model);
		if (ExCalculations.isPointOnScreen(p)) {
			return false;
		}
		final int speed = Mouse.getSpeed();
		Mouse.setSpeed(Random.nextInt(5, 10));
		for (int i = 0; i < 10 && model != null
				&& ExCalculations.isPointOnScreen(p); i += 1) {
			p = ExCalculations.getRandomPoint(model);
			if (!ExCalculations.isPointOnScreen(p)) {
				return false;
			}
			Mouse.move(p);
			Task.sleep(150, 200);
			if (Menu.contains(action)) {
				Mouse.setSpeed(speed);
				return Menu.click(action);
			}
		}
		Mouse.setSpeed(speed);
		return false;
	}

	/**
	 * Interacts with the specified npc.
	 * 
	 * @param npc
	 *            The npc to interact with.
	 * @param action
	 *            The action to do.
	 * @return <tt>true</tt> if the npc was interacted with successfully;
	 *         <tt>false</tt> otherwise.
	 */
	public static boolean interact(final Npc npc, final String action) {
		if (npc == null) {
			return false;
		}
		final Model model = npc.getModel();
		if (model == null) {
			if (npc.getLocation() == null) {
				return false;
			}
			return interact(npc.getLocation(), action);
		}
		if (!interact(model, action)) {
			return npc.interact(action);
		} else {
			return true;
		}
	}

	/**
	 * Interacts with the specified tile.
	 * 
	 * @param tile
	 *            The tile to interact with.
	 * @param action
	 *            The action to do.
	 * @return <tt>true</tt> if the tile was interacted with successfully;
	 *         <tt>false</tt> otherwise.
	 */
	public static boolean interact(final Tile tile, final String action) {
		if (tile == null) {
			return false;
		}
		Point p = ExCalculations.getRandomPoint(tile);
		if (!ExCalculations.isPointOnScreen(p)) {
			return false;
		}
		final int speed = Mouse.getSpeed();
		Mouse.setSpeed(Random.nextInt(5, 10));
		for (int i = 0; i < 10 && tile != null && tile.isOnScreen(); i += 1) {
			p = ExCalculations.getRandomPoint(tile);
			if (!ExCalculations.isPointOnScreen(p)) {
				return false;
			}
			Mouse.move(p);
			Task.sleep(150, 200);
			if (Menu.contains(action)) {
				Mouse.setSpeed(speed);
				return Menu.click(action);
			}
		}
		Mouse.setSpeed(speed);
		return false;
	}

	/**
	 * Interacts with the specified tile.
	 * 
	 * @param tile
	 *            The tile to interact with.
	 * @param action
	 *            The action to do.
	 * @param dX
	 *            The x value of were to click on the tile (0.0 - 1.0).
	 * @param dY
	 *            The y value of were to click on the tile (0.0 - 1.0).
	 * @param height
	 *            The height at which the tile is.
	 * @return <tt>true</tt> if the tile was interacted with successfully;
	 *         <tt>false</tt> otherwise.
	 */
	public static boolean interact(final Tile tile, final String action,
			final double dX, final double dY, final int height) {
		if (tile == null) {
			return false;
		}
		Point p = tile.getPoint(dX, dY, height);
		if (!ExCalculations.isPointOnScreen(p)) {
			return false;
		}
		final int speed = Mouse.getSpeed();
		Mouse.setSpeed(Random.nextInt(5, 10));
		for (int i = 0; i < 10 && tile.isOnScreen(); i += 1) {
			p = tile.getPoint(dX + Random.nextDouble(-0.1, 0.1),
					dY + Random.nextDouble(-0.1, 0.1), height);
			if (!ExCalculations.isPointOnScreen(p)) {
				return false;
			}
			if (Menu.isOpen() && !Menu.contains(action.toLowerCase())) {
				Mouse.moveRandomly(750);
			}
			Mouse.move(p);
			Task.sleep(150, 200);
			if (Menu.contains(action)) {
				Mouse.setSpeed(speed);
				return Menu.click(action);
			}
		}
		Mouse.setSpeed(speed);
		return false;
	}

	/**
	 * Moves the mouse off the screen for 5 - 10 seconds, then moves the mouse
	 * back to a point close to where it was.
	 */
	public static void moveOffScreen() {
		moveOffScreen(Random.nextInt(5000, 10000));
	}

	/**
	 * Moves the mouse off the screen for the specified amount of time in
	 * milliseconds, then moves the mouse back to a point close to where it was.
	 * 
	 * @param waitTime
	 *            The time in millseconds before the mouse is moved back on the
	 *            screen.
	 */
	public static void moveOffScreen(final int waitTime) {
		final int speed = Mouse.getSpeed();
		final Point mouse = Mouse.getLocation();
		if (!Mouse.isPresent()) {
			return;
		}
		switch (Random.nextInt(0, 2)) {
			case 0:
				Mouse.setSpeed(Random.nextInt(0, 4));
				Mouse.moveOffScreen();
				Task.sleep(waitTime);
				Mouse.setSpeed(Math.max(0,
						Math.min(9, speed + Random.nextInt(-1, 2))));
				Mouse.move(mouse.x + Random.nextInt(-10, 10),
						mouse.y + Random.nextInt(-10, 10));
				Mouse.setSpeed(speed);
				break;
			case 1:
				final int tempSpeed = Random.nextInt(0, 10);
				Mouse.setSpeed(tempSpeed);
				Mouse.moveOffScreen();
				Task.sleep(waitTime);
				Mouse.setSpeed(Math.min(9,
						Math.max(0, tempSpeed + Random.nextInt(-1, 2))));
				Mouse.move(mouse.x + Random.nextInt(-10, 10),
						mouse.y + Random.nextInt(-10, 10));
				Mouse.setSpeed(speed);
				break;
		}
	}

	/**
	 * Opens the specified tabs. Waits for 5 - 10 seconds before opening the
	 * next tab.
	 * 
	 * @param tabs
	 *            The tabs to open. Use Game.TAB_...
	 */
	public static void openTabs(final int[] tabs) {
		openTabs(tabs, Random.nextInt(5000, 10000));
	}

	/**
	 * Opens the specified tabs and waits before opening the next tab with a
	 * given time.
	 * 
	 * @param tabs
	 *            The tabs to open. Use Game.TAB_...
	 * @param waitTime
	 *            The time in milliseconds to wait before opening the next tab.
	 */
	public static void openTabs(final int[] tabs, final int waitTime) {
		for (final int tab : tabs) {
			if (Game.getCurrentTab() != tab) {
				Game.openTab(tab);
			}
			Task.sleep(waitTime);
		}
	}

	/**
	 * Opens a number(x) of tabs. Waits for 5 - 10 seconds before opening the
	 * next tab.
	 * 
	 * @param x
	 *            The number of tabs to open.
	 */
	public static void openXTabs(final int x) {
		openXTabs(x, Random.nextInt(5000, 10000));
	}

	/**
	 * Opens a number(x) of tabs and waits before opening the next tab with a
	 * given time.
	 * 
	 * @param x
	 *            The number of tabs to open.
	 * @param waitTime
	 *            The time in milliseconds to wait before opening the next tab.
	 */
	public static void openXTabs(final int x, final int waitTime) {
		for (int i = 0; i < x; i += 1) {
			final int tab = Random.nextInt(0, 17);
			if (Game.getCurrentTab() != tab) {
				Game.openTab(tab);
			}
			Task.sleep(waitTime);
		}
	}

	private static int toColor(final double d) {
		return Math.min(255, Math.max(0, (int) d));
	}

	/**
	 * Randomly wiggles the mouse.
	 */
	public static void wiggle() {
		wiggle(1, 750);
	}

	/**
	 * Randomly wiggles the mouse with a distance restriction.
	 * 
	 * @param maxDistance
	 *            The maximum distance the mouse is able to mouse.
	 */
	public static void wiggle(final int maxDistance) {
		wiggle(1, maxDistance);
	}

	/**
	 * Randomly wiggles the mouse within the specified distance.
	 * 
	 * @param minDistance
	 *            The minimum distance the mouse has to move.
	 * @param maxDistance
	 *            The maximum distance the mouse is able to move.
	 */
	public static void wiggle(final int minDistance, final int maxDistance) {
		final int speed = Mouse.getSpeed();
		final Point mouse = Mouse.getLocation();
		if (!Mouse.isPresent()) {
			return;
		}
		switch (Random.nextInt(0, 4)) {
			case 0:
				Mouse.setSpeed(Random.nextInt(0, 4));
				Mouse.move(generateRandomPoint(minDistance, maxDistance));
				Mouse.setSpeed(Math.max(0,
						Math.min(9, speed + Random.nextInt(-1, 2))));
				Mouse.move(mouse.x + Random.nextInt(-10, 10),
						mouse.y + Random.nextInt(-10, 10));
				Mouse.setSpeed(speed);
				break;
			case 1:
				Mouse.setSpeed(Random.nextInt(0, 4));
				Mouse.move(generateRandomPoint(minDistance, maxDistance));
				Mouse.setSpeed(speed);
				break;
			case 2:
				Mouse.setSpeed(Random.nextInt(0, 4));
				Mouse.move(generateRandomPoint(minDistance, maxDistance));
				Mouse.setSpeed(Random.nextInt(0, 4));
				Mouse.move(generateRandomPoint(minDistance, maxDistance));
				Mouse.setSpeed(Math.max(0,
						Math.min(9, speed + Random.nextInt(-1, 2))));
				Mouse.move(mouse.x + Random.nextInt(-10, 10),
						mouse.y + Random.nextInt(-10, 10));
				Mouse.setSpeed(speed);
				break;
			case 3:
				Mouse.setSpeed(Random.nextInt(0, 4));
				Mouse.move(generateRandomPoint(minDistance, maxDistance));
				Mouse.setSpeed(Random.nextInt(0, 4));
				Mouse.move(generateRandomPoint(minDistance, maxDistance));
				Mouse.setSpeed(speed);
				break;
		}
	}
}
