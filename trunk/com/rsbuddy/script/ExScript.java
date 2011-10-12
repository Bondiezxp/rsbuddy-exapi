package com.rsbuddy.script;

import com.rsbuddy.event.events.AnimationEvent;
import com.rsbuddy.event.events.HealthEvent;
import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.events.SettingEvent;
import com.rsbuddy.event.events.StanceEvent;
import com.rsbuddy.event.listeners.CharacterListener;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.event.listeners.SettingListener;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.methods.Settings;
import com.rsbuddy.script.wrappers.Character;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author Ramus
 */
public abstract class ExScript extends ActiveScript implements CharacterListener, KeyListener, MessageListener,
		MouseListener, MouseMotionListener, PaintListener, SettingListener {

	/**
	 * Sleeps until condition is true or until the timeout is reached
	 * 
	 * @param timeout
	 *            The time to sleep for if the condition is always false.
	 * @param condition
	 *            The conditon to sleep for.
	 */
	public static final void sleep(final int timeout, final boolean condition) {
		final int threshold = 10;
		for (int i = 0; !condition && i < timeout / 10; i += 1) {
			sleep(threshold);
		}
	}

	private final LinkedHashMap<Object, Integer[]> cache = new LinkedHashMap<Object, Integer[]>();

	private void check() {
		if (!Game.isLoggedIn()) {
			return;
		}
		final LinkedList<Character[]> chars = new LinkedList<Character[]>();
		chars.add(Npcs.getLoaded());
		chars.add(Players.getLoaded());
		for (int i = 0; i < chars.size(); i += 1) {
			for (final Character c : chars.get(i)) {
				if (c == null) {
					continue;
				}
				final int anim = c.getAnimation();
				final int hpp = c.getHpPercent();
				final int stance = c.getStance();
				Integer[] data = cache.get(c);
				if (data == null || data.length < 1) {
					data = new Integer[6];
					data[0] = -1;
					data[1] = anim;
					data[2] = -1;
					data[3] = hpp;
					data[4] = -1;
					data[5] = stance;
					cache.put(c, data);
					continue;
				}
				if (data[1] != anim) {
					data[0] = data[1];
					data[1] = anim;
					onAnimationChange(new AnimationEvent(c, data[0], data[1]));
				}
				if (data[3] != hpp) {
					data[2] = data[3];
					data[3] = hpp;
					onHealthChange(new HealthEvent(c, data[2], data[3]));
				}
				if (data[5] != stance) {
					data[4] = data[5];
					data[5] = stance;
					onStanceChange(new StanceEvent(c, data[4], data[5]));
				}
				cache.put(c, data);
			}
		}
		for (int i = 0; i < Settings.getArray().length; i += 1) {
			final int setting = Settings.getArray()[i];
			Integer[] data = cache.get(i);
			if (data == null || data.length < 1) {
				data = new Integer[2];
				data[0] = -1;
				data[1] = setting;
				cache.put(i, data);
				continue;
			}
			if (data[1] != setting) {
				data[0] = data[1];
				data[1] = setting;
				onValueChange(new SettingEvent(i, data[0], data[1]));
			}
			cache.put(i, data);
		}
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	}

	@Override
	public void keyReleased(final KeyEvent e) {
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	}

	@Override
	public final int loop() {
		check();
		return onLoop();
	}

	@Override
	public void messageReceived(final MessageEvent e) {
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	@Override
	public void onAnimationChange(final AnimationEvent e) {
	}

	@Override
	public void onFinish() {
	}

	@Override
	public void onHealthChange(final HealthEvent e) {
	}

	public abstract int onLoop();

	@Override
	public void onRepaint(final Graphics g) {
	}

	@Override
	public void onStanceChange(final StanceEvent e) {
	}

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public void onValueChange(final SettingEvent e) {
	}
}