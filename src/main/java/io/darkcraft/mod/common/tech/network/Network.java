package io.darkcraft.mod.common.tech.network;

import gnu.trove.set.hash.THashSet;
import io.darkcraft.api.tech.network.INetwork;
import io.darkcraft.api.tech.network.INetworkTE;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class Network implements INetwork
{
	private final long id;
	private final NetworkRegistry netReg;
	private int energy;
	public Network(long id, NetworkRegistry netReg)
	{
		this.id = id;
		this.netReg = netReg;
	}

	@Override
	public long getID()
	{
		return id;
	}

	@Override
	public int getEnergy()
	{
		return energy;
	}

	@Override
	public int getMaxEnergy()
	{
		return 10000000;
	}

	private Set<SimpleCoordStore> teSet = new THashSet<SimpleCoordStore>();

	public boolean containsTE(SimpleCoordStore scs)
	{
		return teSet.contains(scs);
	}

	public boolean canReach(SimpleCoordStore scs)
	{
		if(containsTE(scs)) return true;
		for(SimpleCoordStore nScs : teSet)
			if(NetworkHelper.canReach(nScs, scs))
				return true;
		return false;
	}

	@Override
	public Set<SimpleCoordStore> getNetworkTEs()
	{
		return Collections.unmodifiableSet(teSet);
	}

	protected void addTE(INetworkTE te)
	{
		teSet.add(te.coords());
	}

	private void removeEmpties()
	{
		Iterator<SimpleCoordStore> iter = teSet.iterator();
		while(iter.hasNext())
		{
			SimpleCoordStore scs = iter.next();
			if(!scs.isLoaded()) continue; //Don't necessarily need to keep loading chunks, if unloaded, assume still exists
			if(scs.getTileEntity() instanceof INetworkTE) continue;
			iter.remove();
		}
	}

	private void validate()
	{
		removeEmpties();
		if(teSet.size() <= 1) return;
		SimpleCoordStore[] scsArr = teSet.toArray(new SimpleCoordStore[teSet.size()]);
		int[] llArr = new int[scsArr.length];
		for(int i = 0; i < scsArr.length; i++)
			llArr[i] = Integer.MAX_VALUE;
		for(int i = 0; i < scsArr.length; i++)
			if(llArr[i] == Integer.MAX_VALUE)
				tarjan(scsArr,llArr,i);
		Network[] nets = new Network[scsArr.length];
		nets[0] = this;
		int netCount = 1;
		for(int i = 1; i < scsArr.length; i++)
		{
			if(llArr[i] == llArr[i-1])
				nets[i] = nets[i-1];
			else
			{
				nets[i] = netReg.getNewNetwork();
				netCount++;
			}
			if(nets[i] != this)
			{
				INetworkTE te = (INetworkTE) scsArr[i].getTileEntity();
				te.setNetwork(nets[i]);
			}
		}
		int newEn = energy / netCount;
		for(Network n : nets)
			n.energy = newEn;
	}

	private void tarjan(SimpleCoordStore[] scsArr, int[] llArr, int index)
	{
		llArr[index] = Math.min(index,llArr[index]);
		for(int i = 0; i < scsArr.length; i++)
		{
			if(index == i) continue;
			if(!(NetworkHelper.canReach(scsArr[i],scsArr[index]))) continue;
			if(llArr[i] > llArr[index])
			{
				llArr[i] = llArr[index];
				tarjan(scsArr,llArr,index);
			}
		}
	}

	private int tt = 0;
	public void tick()
	{
		tt++;
		if((tt % 600) == 0)
			validate();
		if((tt % 10) == 0)
			transfer();
	}

	private void transfer()
	{
		Set<INetworkTE> toProc = new THashSet<INetworkTE>();
		for(SimpleCoordStore scs : teSet)
		{
			if(!scs.isLoaded()) continue;
			TileEntity te = scs.getTileEntity();
			if(te instanceof INetworkTE)
				toProc.add((INetworkTE) te);
		}
		int max = getMaxEnergy();
		for(INetworkTE nte : toProc)
			if(nte.canExportPower(this))
			{
				int desired = Math.min(max-energy,nte.getExportablePower(this));
				energy = Math.min(max, energy + nte.exportPower(this, desired));
			}
		for(INetworkTE nte : toProc)
			if(nte.canImportPower(this) && !nte.canExportPower(this))
				energy -= nte.importPower(this, Math.min(energy, nte.getRequestedPower(this)));
		for(INetworkTE nte : toProc)
			if(nte.canImportPower(this) && nte.canExportPower(this))
				energy -= nte.importPower(this, Math.min(energy, nte.getRequestedPower(this)));
	}

	public void writeToNBT(NBTTagCompound mnbt, String n)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("id", id);
		nbt.setInteger("energy", energy);
		int i = 0;
		for(SimpleCoordStore scs : teSet)
			scs.writeToNBT(nbt, "scs"+(i++));
		mnbt.setTag(n, nbt);
	}

	public static Network readFromNBT(NetworkRegistry nr, NBTTagCompound nbt)
	{
		long id = nbt.getLong("id");
		int en = nbt.getInteger("en");
		Network n = new Network(id,nr);
		n.energy = en;
		int i = 0;
		while(nbt.hasKey("scs"+i))
			n.teSet.add(SimpleCoordStore.readFromNBT(nbt, "scs"+(i++)));
		return n;
	}

	@Override
	public String toString()
	{
		return "{N:<"+id+"/"+netReg.getDimension()+"> " + getEnergy() +"/" + getMaxEnergy() +"}";
	}

}
