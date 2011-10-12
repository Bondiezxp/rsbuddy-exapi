package com.rsbuddy.event.listeners;

import com.rsbuddy.event.events.AnimationEvent;
import com.rsbuddy.event.events.HealthEvent;
import com.rsbuddy.event.events.StanceEvent;

/**
 * @author Ramus
 */
public interface CharacterListener {

	/**
	 * Triggered when a characters' animation changes.
	 * 
	 * @param e
	 *            The animation event which was triggered.
	 */
	public abstract void onAnimationChange(final AnimationEvent e);

	/**
	 * Triggered when a characters' health percentage changes.
	 * 
	 * @param e
	 *            The health event which was triggered.
	 */
	public abstract void onHealthChange(final HealthEvent e);

	/**
	 * Triggered when a characters' stance changes.
	 * 
	 * @param e
	 *            The stance event which was triggered.
	 */
	public abstract void onStanceChange(final StanceEvent e);
}