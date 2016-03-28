package io.darkcraft.mod.common.magic.component;

public interface IDurationComponent
{
	/**
	 * @return minimum number of times the effect will be applied
	 */
	public int getMinDuration();

	/**
	 * @return maximum number of times the effect will be applied
	 */
	public int getMaxDuration();

	/**
	 * Allows a spell component to alter the cost of the component based on the duration of the effect.
	 * @return how much the component should cost with this duration
	 */
	public double getCostDur(int duration, double oldCost);
}
