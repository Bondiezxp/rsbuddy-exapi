package com.rsbuddy.script.action;

import com.rsbuddy.script.task.LoopTask;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class ActionHandler extends LoopTask implements ActionListener {

	private final List<Action> actions = Collections.synchronizedList(new LinkedList<Action>());
	private static Action currentAction = null;

	/**
	 * Gets the current action.
	 * 
	 * @return The current action.
	 */
	public static Action getCurrentAction() {
		return currentAction;
	}

	/**
	 * Gets the status of the current action.
	 * 
	 * @return The current status of the current action.
	 */
	public static String getStatus() {
		if (currentAction == null || currentAction.getStatus() == null) {
			return "";
		}
		return currentAction.getStatus();
	}

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
		try {
			for (final Action act : actions) {
				if (!act.activate()) {
					stop(act);
					continue;
				}
				act.execute();
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
		actions.remove(action);
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
	 * Stops all active actions in this handler.
	 */
	public void stopAll() {
		for (final Action act : actions) {
			act.active = false;
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
		if (actions.contains(action)) {
			return true;
		}
		action.setHandler(this);
		return actions.add(action);
	}
}