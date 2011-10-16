package com.rsbuddy.script.action;

import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Condition;

import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ramus
 */
public abstract class Action extends Task implements PaintListener {

	/**
	 * The minimum priority that an action can have.
	 */
	public final static int MIN_PRIORITY = 1;

	/**
	 * The default priority that is assigned to an action.
	 */
	public final static int NORM_PRIORITY = 5;

	/**
	 * The maximum priority that an action can have.
	 */
	public final static int MAX_PRIORITY = 10;

	/**
	 * Returns a reference to the currently executing action object.
	 * 
	 * @return The currently executing action.
	 */
	public static Action currentAction() {
		return ActionHandler.currentAction;
	}

	/**
	 * For autonumbering anonymous actions.
	 * 
	 * @return The number for the next action.
	 */
	private static synchronized int nextActionNum() {
		return actionInitNumber += 1;
	}

	/**
	 * Sleeps until either the condition is valid or the time is reached.
	 * 
	 * @param time
	 *            The timeout.
	 * @param condition
	 *            The condition to wait for.
	 * @see #sleep(int, int, Condition)
	 */
	public static void sleep(final int time, final Condition condition) {
		final int threshold = 10;
		for (int i = 0; !condition.isValid() && i < time / threshold; i += 1) {
			sleep(threshold);
		}
	}

	/**
	 * Sleeps until either the condition is valid or the time is reached with
	 * the given threshold.
	 * 
	 * @param time
	 *            The time before the next check is made.
	 * @param threshold
	 *            The threshold to sleep before the next check (I use 10).
	 * @param condition
	 *            The condition to wait for.
	 */
	public static void sleep(final int time, final int threshold, final Condition condition) {
		for (int i = 0; !condition.isValid() && i < time / threshold; i += 1) {
			sleep(threshold);
		}
	}

	private int priority;
	protected boolean active;

	private char[] name;

	private ActionHandler handler;

	private static int actionInitNumber;

	private static final Logger log = Logger.getLogger(Action.class.getName());

	/**
	 * Creates a new action with an automated name.
	 */
	public Action() {
		this("Action-" + nextActionNum());
	}

	/**
	 * Creates a new action with the specified name.
	 * 
	 * @param name
	 *            The name of the action.
	 */
	public Action(final String name) {
		this.name = name.toCharArray();
		final Action parent = currentAction();
		if (parent != null) {
			priority = parent.getPriority();
		} else {
			priority = NORM_PRIORITY;
		}
	}

	/**
	 * Checks wether or not this action should activate.
	 * 
	 * @return <tt>true</tt> if this action should be activated; <tt>false</tt>
	 *         otherwise.
	 */
	public abstract boolean activate();

	/**
	 * Invoked when this action is activated.
	 * 
	 * @see #onExecute()
	 */
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
				log.log(Level.SEVERE, "Unhandled exception in " + this, e);
				handler.stopAll();
			}
		}
		handler.onActionStopped(this);
	}

	/**
	 * Gets this actions' current handler.
	 * 
	 * @return The current handler for this action.
	 * @see #setHandler(ActionHandler)
	 */
	public ActionHandler getHandler() {
		return handler;
	}

	/**
	 * Gets this actions current name.
	 * 
	 * @return The actions name.
	 */
	public String getName() {
		return String.valueOf(name);
	}

	/**
	 * Returns this action's priority.
	 * 
	 * @return this action's priority.
	 * @see #setPriority(int)
	 */
	public final int getPriority() {
		return priority;
	}

	/**
	 * Gets this actions current status.
	 * 
	 * @return The actions status.
	 */
	public abstract String getStatus();

	/**
	 * This is what the action should do on activation.
	 */
	public abstract void onExecute();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRepaint(final Graphics g) {
	}

	/**
	 * Sets this action's current handler.
	 * 
	 * @param handler
	 *            The handler to set this action's to.
	 * @see #getHandler()
	 */
	public void setHandler(final ActionHandler handler) {
		this.handler = handler;
	}

	/**
	 * Changes the name of this action.
	 * 
	 * @param name
	 *            The new name for this action.
	 * @see #getName()
	 */
	public final void setName(final String name) {
		this.name = name.toCharArray();
	}

	/**
	 * Changes the priority of this action.
	 * 
	 * @param newPriority
	 *            The priority to set this action to.
	 * @exception IllegalArgumentException
	 *                If the priority is not in the range
	 *                <code>MIN_PRIORITY</code> to <code>MAX_PRIORITY</code>.
	 * @see #getPriority()
	 * @see #MAX_PRIORITY
	 * @see #MIN_PRIORITY
	 */
	public final void setPriority(final int newPriority) {
		if (newPriority > MAX_PRIORITY || newPriority < MIN_PRIORITY) {
			throw new IllegalArgumentException();
		}
		priority = newPriority;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Action[" + getName() + ", " + getPriority() + "]";
	}
}