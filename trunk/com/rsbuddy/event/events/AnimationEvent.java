package com.rsbuddy.event.events;

import com.rsbuddy.script.wrappers.Character;

public class AnimationEvent {

	private final Character character;
	private final int previousAnimation;
	private final int currentAnimation;

	public AnimationEvent(final Character character,
			final int previousAnimation, final int currentAnimation) {
		this.character = character;
		this.previousAnimation = previousAnimation;
		this.currentAnimation = currentAnimation;
	}

	/**
	 * The character of which the animation changed.
	 * 
	 * @return The character of which the animation changed.
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * The characters' current animation.
	 * 
	 * @return The current animation of the character.
	 */
	public int getCurrentAnimation() {
		return currentAnimation;
	}

	/**
	 * The characters' previous animation.
	 * 
	 * @return The previous animation of the character.
	 */
	public int getPreviousAnimation() {
		return previousAnimation;
	}
}