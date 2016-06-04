package io.darkcraft.api.tech.network;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

import java.util.Set;

public interface INetwork
{
	public long getID();

	public int getEnergy();

	public int getMaxEnergy();

	public Set<SimpleCoordStore> getNetworkTEs();
}
