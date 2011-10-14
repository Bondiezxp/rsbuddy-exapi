package com.rsbuddy.script.action;

import com.rsbuddy.script.task.LoopTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class ActionHandler extends LoopTask implements ActionListener {

	private class ActionComparator implements Comparator<Action> {

		@Override
		public int compare(final Action a1, final Action a2) {
			final int diff = ((Integer) a1.getPriority()).compareTo(a2.getPriority());
			return diff < 0 ? 1 : diff > 0 ? -1 : 0;
		}
	}

	private final List<Action> actions = Collections.synchronizedList(new LinkedList<Action>());
	protected static Action currentAction = null;

	private boolean running;

	/**
	 * Gets all of the active actions.
	 * 
	 * @return All of the active actions in this handler.
	 */
	public List<Action> getActions() {
		return Collections.unmodifiableList(actions);
	}

	@Override
	public int loop() {
		if (!running) {
			return -1;
		}
		try {
			synchronized (actions) {
				Collections.sort(actions, new ActionComparator());
				for (final Action act : actions) {
					if (!act.activate()) {
						stop(act);
						continue;
					}
					act.execute();
				}
			}
		} catch (final ConcurrentModificationException e) {
			return 0;
		}
		return 0;
	}

	@Override
	public void onActionStarted(final Action action) {
		currentAction = action;
	}

	@Override
	public void onActionStopped(final Action action) {
		synchronized (actions) {
			actions.remove(action);
		}
	}

	@Override
	public boolean onStart() {
		running = true;
		return true;
	}

	/**
	 * Stops the specified action.
	 * 
	 * @param action
	 *            The action to stop.
	 * @return <tt>true</tt> if the action was stopped. <tt>false</tt>
	 *         otherwise.
	 */
	public boolean stop(final Action action) {
		action.active = false;
		return !action.active;
	}

	/**
	 * Stops all active actions in this handler and deactivates this handler.
	 */
	public void stopAll() {
		synchronized (actions) {
			for (final Action act : actions) {
				act.active = false;
			}
			running = false;
		}
	}

	/**
	 * Submits a new action to this handler. If the handler already contains it,
	 * it will not add it.
	 * 
	 * @param action
	 *            The action to add.
	 * @return <tt>true</tt> if the action was added. <tt>false</tt> otherwise.
	 */
	public boolean submit(final Action action) {
		synchronized (actions) {
			if (actions.contains(action)) {
				return true;
			}
			action.setHandler(this);
			return actions.add(action);
		}
	}
}