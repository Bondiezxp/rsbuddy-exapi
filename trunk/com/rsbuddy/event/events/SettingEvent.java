package com.rsbuddy.event.events;

/**
 * @author Ramus
 */
public class SettingEvent {

	private final int settingIndex;
	private final int previousValue;
	private final int currentValue;

	public SettingEvent(final int settingIndex, final int previousValue, final int currentValue) {
		this.settingIndex = settingIndex;
		this.previousValue = previousValue;
		this.currentValue = currentValue;
	}

	/**
	 * The settings' current value.
	 * 
	 * @return The current value of the setting.
	 */
	public int getCurrentValue() {
		return currentValue;
	}

	/**
	 * The index of the setting of which values had changed.
	 * 
	 * @return The index of the setting.
	 */
	public int getIndex() {
		return settingIndex;
	}

	/**
	 * The settings' previous value.
	 * 
	 * @return The previous value of the setting.
	 */
	public int getPreviousValue() {
		return previousValue;
	}
}