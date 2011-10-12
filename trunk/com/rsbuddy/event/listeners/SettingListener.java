package com.rsbuddy.event.listeners;

import com.rsbuddy.event.events.SettingEvent;

/**
 * @author Ramus
 */
public interface SettingListener {

	/**
	 * Triggered when a settings' value changes.
	 * 
	 * @param e
	 *            The setting event which was triggered.
	 */
	public abstract void onValueChange(final SettingEvent e);
}
