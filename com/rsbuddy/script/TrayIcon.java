package com.rsbuddy.script;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Ramus
 */
public class TrayIcon {

	private final java.awt.TrayIcon trayIcon;

	public TrayIcon(final Image image) {
		trayIcon = new java.awt.TrayIcon(image);
		if (!SystemTray.isSupported()) {
			try {
				throw new RuntimeException(
						"System tray is not supported, if you hide the program you may not be able to restore it.");
			} catch (final Exception e) {
				return;
			}
		}
	}

	/**
	 * Adds an action listener to the tray icon.
	 * 
	 * @param listener
	 *            The action listener to add.
	 */
	public void addActionListener(final ActionListener listener) {
		trayIcon.addActionListener(listener);
	}

	/**
	 * Adds mouse listener to the tray icon.
	 * 
	 * @param listener
	 *            The mouse listener to add.
	 */
	public void addMouseListener(final MouseListener listener) {
		trayIcon.addMouseListener(listener);
	}

	/**
	 * Adds a mouse motion listener to the tray icon.
	 * 
	 * @param listener
	 *            The mouse motion listener to add.
	 */
	public void addMouseMotionListener(final MouseMotionListener listener) {
		trayIcon.addMouseMotionListener(listener);
	}

	/**
	 * Adds the tray icon to the system tray if supported.
	 */
	public void addToSystemTray() {
		if (inSystemTray()) {
			return;
		}
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (final Exception e) {
		}
	}

	/**
	 * Displays a message above the tray icon.
	 * 
	 * @param title
	 *            The title of the message.
	 * @param text
	 *            The main body text of the message.
	 * @param messageType
	 *            The message type.
	 */
	public void displayMessage(final String title, final String text, final MessageType messageType) {
		if (!inSystemTray()) {
			return;
		}
		trayIcon.displayMessage(title, text, messageType);
	}

	/**
	 * Wether or not the tray icon is in the system tray.
	 * 
	 * @return <tt>true</tt> if the tray icon is in the system tray;
	 *         <tt>false</tt> otherwise.
	 */
	public boolean inSystemTray() {
		for (final java.awt.TrayIcon ti : SystemTray.getSystemTray().getTrayIcons()) {
			if (ti.equals(trayIcon)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the tray icon from the system tray.
	 */
	public void removeFromSystemTray() {
		if (!inSystemTray()) {
			return;
		}
		SystemTray.getSystemTray().remove(trayIcon);
	}

	/**
	 * Sets the tray icons' popup menu.
	 * 
	 * @param popup
	 *            The popup menu.
	 */
	public void setPopupMenu(final PopupMenu popup) {
		trayIcon.setPopupMenu(popup);
	}

	/**
	 * Sets the tray icons' tooltip.
	 * 
	 * @param tooltip
	 *            The tooltip.
	 */
	public void setToolTip(final String tooltip) {
		trayIcon.setToolTip(tooltip);
	}
}