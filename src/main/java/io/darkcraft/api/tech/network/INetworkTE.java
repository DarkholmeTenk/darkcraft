package io.darkcraft.api.tech.network;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

public interface INetworkTE
{
	public SimpleCoordStore coords();

	/**
	 * Called when a new network is assigned to your TE.
	 * @param network the new network assigned to your TE.
	 */
	public void setNetwork(INetwork network);

	/**
	 * @return true if the TE can push power into the network, e.g. generators
	 */
	public boolean canExportPower(INetwork network);

	/**
	 * @return the amount of power this TE can export to the network
	 */
	public int getExportablePower(INetwork network);

	/**
	 * Called from the network when it's trying to pull power from your TE.
	 * Reduce your internal storage by the amount of power exported.
	 * @param network the network which wants the power
	 * @param amount the amount of power the network wants
	 * @return the amount of power that is to go into the network
	 */
	public int exportPower(INetwork network, int amount);

	/**
	 * @return true if the TE can pull power from the network, e.g. furnaces
	 */
	public boolean canImportPower(INetwork network);

	/**
	 * @return the amount of power this TE wants from the network
	 */
	public int getRequestedPower(INetwork network);

	/**
	 * Called from the network when it wants to push power into your TE.
	 * Increase your internal storage by the amount of power imported.
	 * @param network the network which wants to provide the power
	 * @param amount the amount of power the network wants to provide your TE
	 * @return the amount of power that is accepted from the network
	 */
	public int importPower(INetwork network, int amount);
}
