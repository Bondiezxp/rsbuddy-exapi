package com.rsbuddy.event.events;

import com.rsbuddy.script.wrappers.Character;

/**
 * @author Ramus
 */
public class HealthEvent {

	private final Character character;
	private final int previousHealth;
	private final int currentHealth;

	public HealthEvent(final Character character, final int previousHealth, final int currentHealth) {
		this.character = character;
		this.previousHealth = previousHealth;
		this.currentHealth = currentHealth;
	}

	/**
	 * The character of which the health percentage changed.
	 * 
	 * @return The character of which the health percentage changed.
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * The characters' current health percentage.
	 * 
	 * @return The current health percentage of the character.
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * The characters' previous health percentage.
	 * 
	 * @return The previous health percentage of the character.
	 */
	public int getPreviousHealth() {
		return previousHealth;
	}
}