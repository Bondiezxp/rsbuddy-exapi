package com.rsbuddy.script;

import com.rsbuddy.script.util.Random;

public abstract class Action {

	/**
	 * {@inheritDoc}
	 */
	public static final void sleep(final int time) {
		com.rsbuddy.script.task.Task.sleep(time);
	}

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

	/**
	 * {@inheritDoc}
	 */
	public static final void sleep(final int min, final int max) {
		com.rsbuddy.script.task.Task.sleep(min, max);
	}

	/**
	 * Sleeps until condition is true or until the timeout is reached
	 * 
	 * @param min
	 *            The minimum time to sleep for if the condition is always
	 *            false.
	 * @param max
	 *            The maximum time to sleep for if the condition is always
	 *            false.
	 * @param condition
	 *            The conditon to sleep for.
	 */
	public static final void sleep(final int min, final int max,
			final boolean condition) {
		final int timeout = Random.nextInt(min, max);
		sleep(timeout, condition);
	}

	public abstract void execute();

	public abstract String getStatus();

	public abstract boolean isValid();
}
