package com.rsbuddy.script.util;

public interface Condition {

	/**
	 * Checks whether or not the condition is valid.
	 * 
	 * @return <tt>true</tt> if the condition is valid. <tt>false</tt>
	 *         otherwise.
	 */
	public abstract boolean isValid();
}