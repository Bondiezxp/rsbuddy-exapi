package com.rsbuddy.event.events;

import com.rsbuddy.script.wrappers.Character;

public class StanceEvent {

	private final Character character;
	private final int previousStance;
	private final int currentStance;

	public StanceEvent(final Character character, final int previousStance,
			final int currentStance) {
		this.character = character;
		this.previousStance = previousStance;
		this.currentStance = currentStance;
	}

	/**
	 * The character of which the stance changed.
	 * 
	 * @return The character of which the stance changed.
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * The characters' current stance.
	 * 
	 * @return The current stance of the character.
	 */
	public int getCurrentStance() {
		return currentStance;
	}

	/**
	 * The characters' previous stance.
	 * 
	 * @return The previous stance of the character.
	 */
	public int getPreviousStance() {
		return previousStance;
	}
}