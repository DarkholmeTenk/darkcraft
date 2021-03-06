package io.darkcraft.mod.common.magic.systems.component;

public interface IMagnitudeComponent
{
	public int getMinMagnitude();

	public int getMaxMagnitude();

	/**
	 * Allows a spell component to alter the cost of the component based on the magnitude of the effect.
	 * @return how much the component should cost with this magnitude
	 */
	public double getCostMag(int magnitude, double oldCost);
}
