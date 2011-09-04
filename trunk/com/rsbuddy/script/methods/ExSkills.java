package com.rsbuddy.script.methods;

import java.text.DecimalFormat;

public class ExSkills {

	/**
	 * Gets the exp to the specified level in the given skill.
	 * 
	 * @param skill
	 *            The skill index.
	 * @param level
	 *            The level to get the exp to.
	 * @return The exp to the specified level in the given skill.
	 */
	public static int getExpToLevel(final int skill, final int level) {
		return Skills.XP_TABLE[level] - Skills.getCurrentExp(skill);
	}

	/**
	 * Gets the percent to the given level in the specified skill index with 2
	 * decimal places.
	 * 
	 * @param skill
	 *            The skill index.
	 * @param level
	 *            The level to get the percent to.
	 * @return The percentage to the next level in the specified skill index
	 *         with 2 decimal places.
	 */
	public static double getPercentToLevel(final int skill, final int level) {
		final DecimalFormat df = new DecimalFormat("##.##");
		final int lvl = Skills.getRealLevel(skill);
		if (lvl == 99 && skill != Skills.DUNGEONEERING) {
			return 0;
		} else if (lvl == 120 && skill == Skills.DUNGEONEERING) {
			return 0;
		}
		final double xpTotal = Skills.XP_TABLE[level] - Skills.XP_TABLE[lvl];
		if (xpTotal == 0) {
			return 0;
		}
		final double xpDone = Skills.getCurrentExp(skill)
				- Skills.XP_TABLE[lvl];
		final double progress = 100 * xpDone / xpTotal;
		return Double.valueOf(df.format(progress));
	}

	/**
	 * Gets the percent to the next level in the specified skill index with 2
	 * decimal places.
	 * 
	 * @param skill
	 *            The skill index.
	 * @return The percentage to the next level in the specified skill index
	 *         with 2 decimal places.
	 */
	public static double getPercentToNextLevel(final int skill) {
		return getPercentToNextLevel(Skills.getRealLevel(skill) + 1);
	}

	/**
	 * Gets the time to the specified level in the given skill based on the xp
	 * gained in 1 hour.
	 * 
	 * @param skill
	 *            The skill index.
	 * @param level
	 *            The level you want to find the time to.
	 * @param xpGainedPerHour
	 *            The xp gained in 1 hour.
	 * @return The time to the specified level in the given skill.
	 */
	public static long getTimeToLevel(final int skill, final int level,
			final int xpGainedPerHour) {
		if (xpGainedPerHour < 1) {
			return 0L;
		}
		return getExpToLevel(skill, level) * 3600 / xpGainedPerHour * 1000;
	}

	/**
	 * Gets the time to the specified level in the given skill based on the xp
	 * gained and the run time.
	 * 
	 * @param skill
	 *            The skill index.
	 * @param level
	 *            The level you want to find the time to.
	 * @param runTime
	 *            The amount of time running.
	 * @param xpGained
	 *            The xp gained in the amount of run time.
	 * @return The time to the next level.
	 */
	public static long getTimeToLevel(final int skill, final int level,
			final int runTime, final int xpGained) {
		return getTimeToLevel(skill, level,
				ExCalculations.Paint.getPerHourValue(runTime, xpGained));
	}

	/**
	 * Gets the time to the next level in the specified skill based on the xp
	 * gained in 1 hour.
	 * 
	 * @param skill
	 *            The skill index.
	 * @param xpGainedPerHour
	 *            The xp gained in 1 hour.
	 * @return The time to the next level in the given skill.
	 */
	public static long getTimeToNextLevel(final int skill,
			final int xpGainedPerHour) {
		return getTimeToLevel(skill, Skills.getRealLevel(skill) + 1,
				xpGainedPerHour);
	}

	/**
	 * Gets the time to the next level in the specified skill based on the xp
	 * gained and the run time.
	 * 
	 * @param skill
	 *            The skill index.
	 * @param runTime
	 *            The amount of time running.
	 * @param xpGained
	 *            The xp gained in the amount of run time.
	 * @return The time to the next level.
	 */
	public static long getTimeToNextLevel(final int skill, final long runTime,
			final int xpGained) {
		return getTimeToNextLevel(skill,
				ExCalculations.Paint.getPerHourValue(runTime, xpGained));
	}
}
