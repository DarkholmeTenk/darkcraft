package io.darkcraft.mod.common.magic.thaumion;

public interface IThaumionProducer extends IThaumionNetworkInteractor
{
	public boolean link(IThaumionReceiver receiver);

	public int getMaxThaumionCapacity();

	public int getCurrentThaumionLevel();

	/**
	 *
	 * @param desiredAmount
	 * @param simulate if false do not actually take the thaumions
	 * @return the amount of thaumions provided by the producer
	 */
	public int takeThaumions(int desiredAmount, boolean simulate);
}
