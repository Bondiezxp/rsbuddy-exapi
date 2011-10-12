package com.rsbuddy.script.action;

public interface ActionListener {

	/**
	 * Invoked when an action is started.
	 * 
	 * @param action
	 *            The action that was started.
	 */
	public abstract void onActionStarted(final Action action);

	/**
	 * Invoked when an action is stopped.
	 * 
	 * @param action
	 *            The action that was stopped.
	 */
	public abstract void onActionStopped(final Action action);
}