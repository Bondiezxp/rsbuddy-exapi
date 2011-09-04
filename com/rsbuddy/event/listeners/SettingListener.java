package com.rsbuddy.event.listeners;

public interface SettingListener {

	/**
	 * Triggered when a settings' value changes.
	 * 
	 * @param settingIndex
	 *            The setting index.
	 * @param prevValue
	 *            The previous value for this setting.
	 * @param currValue
	 *            The current value for this setting.
	 */
	public void onSettingChange(final int settingIndex, final int prevValue,
			final int currValue);
}
