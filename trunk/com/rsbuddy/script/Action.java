package com.rsbuddy.script;

import com.rsbuddy.script.util.Random;

public abstract class Action {

	public static final void sleep(final int time) {
		com.rsbuddy.script.task.Task.sleep(time);
	}

	public static final void sleep(final int timeout, final boolean condition) {
		for (int i = 0; !condition && i < timeout; i += 1) {
			sleep(1);
		}
	}

	public static final void sleep(final int min, final int max) {
		com.rsbuddy.script.task.Task.sleep(min, max);
	}

	public static final void sleep(final int min, final int max,
			final boolean condition) {
		final int timeout = Random.nextInt(min, max);
		sleep(timeout, condition);
	}

	public abstract void execute();

	public String getStatus() {
		return "";
	}

	public abstract boolean isValid();
}
