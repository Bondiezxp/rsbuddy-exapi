package com.rsbuddy.script.action;

import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Condition;

/**
 * @author Ramus
 */
public abstract class Action extends Task {

	/**
	 * Sleeps until the either the condition is valid or the time is reached.
	 * 
	 * @param time
	 *            The timeout.
	 * @param condition
	 *            The condition to wait for.
	 */
	public static void sleep(final int time, final Condition condition) {
		final int threshold = 10;
		for (int i = 0; !condition.isValid() && i < time / threshold; i += 1) {
			sleep(threshold);
		}
	}

	/**
	 * Sleeps until the either the condition is valid or the time is reached.
	 * 
	 * @param time
	 *            The time before the next check is made (I use 10).
	 * @param threshold
	 *            The threshold.
	 * @param condition
	 *            The condition to wait for.
	 */
	public static void sleep(final int time, final int threshold, final Condition condition) {
		for (int i = 0; !condition.isValid() && i < time / threshold; i += 1) {
			sleep(threshold);
		}
	}

	protected boolean active = false;

	private ActionHandler handler = null;

	public abstract boolean activate();

	@Override
	public final void execute() {
		active = true;
		if (!activate()) {
			active = false;
			return;
		}
		handler.onActionStarted(this);
		while (active) {
			try {
				onExecute();
				active = false;
			} catch (final Exception e) {
				e.printStackTrace();
				active = false;
			}
		}
		handler.onActionStopped(this);
	}

	/**
	 * Gets this actions' current handler.
	 * 
	 * @return The current handler for this action.
	 */
	public ActionHandler getHandler() {
		return handler;
	}

	public abstract String getStatus();

	public abstract void onExecute();

	/**
	 * Sets this actions' current handler.
	 * 
	 * @param handler
	 *            The handler to set this actions' handler.
	 */
	public void setHandler(final ActionHandler handler) {
		this.handler = handler;
	}
}