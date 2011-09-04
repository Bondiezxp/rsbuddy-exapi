package com.rsbuddy.event.listeners;

import com.rsbuddy.script.wrappers.Character;

public interface CharacterListener {

	/**
	 * Triggered when a characters' animation changes.
	 * 
	 * @param c
	 *            The character.
	 * @param prevAnim
	 *            The previous animation.
	 * @param currAnim
	 *            The current animation.
	 */
	public void onAnimationChange(final Character c, final int prevAnim,
			final int currAnim);

	/**
	 * Triggered when a characters' health percentage changes.
	 * 
	 * @param c
	 *            The character.
	 * @param prevHealth
	 *            The previous health percentage.
	 * @param currHealth
	 *            The current health percentage.
	 */
	public void onHealthChange(final Character c, final int prevHealth,
			final int currHealth);

	/**
	 * Triggered when a characters' stance changes.
	 * 
	 * @param c
	 *            The character.
	 * @param prevStance
	 *            The previous stance.
	 * @param currStance
	 *            The current stance.
	 */
	public void onStanceChange(final Character c, final int prevStance,
			final int currStance);
}
