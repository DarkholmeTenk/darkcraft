package io.darkcraft.mod.common.tech.network;

import gnu.trove.map.hash.THashMap;
import io.darkcraft.api.tech.network.INetworkTE;
import io.darkcraft.darkcore.mod.abstracts.AbstractWorldDataStore;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;

public class NetworkRegistry extends AbstractWorldDataStore
{
	public NetworkRegistry(String name)
	{
		super(name);
	}

	public NetworkRegistry(int dim)
	{
		super("dcTechNet", dim);
	}

	private Map<Long,Network> networks = new THashMap<Long,Network>();
	private long highestID = 0;

	protected Network getNewNetwork()
	{
		Network n = new Network(++highestID,this);
		networks.put(n.getID(),n);
		markDirty();
		return n;
	}

	private Network getNetwork(INetworkTE te)
	{
		SimpleCoordStore scs = te.coords();
		if(scs == null) throw new RuntimeException("Tried to assign network to TE with null coords");
		for(Network n : networks.values())
			if(n.canReach(scs))
				return n;
		return getNewNetwork();
	}

	public void assignNetwork(INetworkTE te)
	{
		Network n = getNetwork(te);
		n.addTE(te);
		te.setNetwork(n);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		highestID = nbt.getLong("high");
		long i = 0;
		while(nbt.hasKey("n"+i))
		{
			Network n = Network.readFromNBT(this, nbt.getCompoundTag("n"+i));
			networks.put(n.getID(), n);
			i++;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("high", highestID);
		long i = 0;
		for(Network n : networks.values())
			if(n.getNetworkTEs().size() > 0)
				n.writeToNBT(nbt, "n"+(i++));
	}

	public void tick()
	{
		for(Network n : networks.values())
			n.tick();
		Iterator<Entry<Long,Network>> iter = networks.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<Long,Network> e = iter.next();
			if(e.getValue().getNetworkTEs().isEmpty()) iter.remove();
		}
		markDirty();
	}
}
