package io.darkcraft.mod.common.magic.thaumion;

public interface IThaumionReceiver extends IThaumionNetworkInteractor
{
	public boolean link(IThaumionProducer producer);

	/**
	 * @param desiredAmount
	 * @param sim
	 * @return the amount of thaumions left over
	 */
	public int giveThaumions(int desiredAmount, boolean sim);
}
