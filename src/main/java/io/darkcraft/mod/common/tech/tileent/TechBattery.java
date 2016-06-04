package io.darkcraft.mod.common.tech.tileent;

import io.darkcraft.api.tech.network.INetwork;
import io.darkcraft.api.tech.network.INetworkTE;
import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.common.tech.network.NetworkHelper;
import io.darkcraft.mod.common.tech.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TechBattery extends AbstractTileEntity implements INetworkTE, IActivatable
{
	private INetwork net;
	private int max = 100000;
	private int energy;

	@Override
	public void tick()
	{
		if((net == null) && ServerHelper.isServer())
		{
			NetworkRegistry nr = NetworkHelper.getNR(WorldHelper.getWorldID(this));
			nr.assignNetwork(this);
		}
	}

	@Override
	public void setNetwork(INetwork network){net = network;}

	@Override
	public boolean canExportPower(INetwork network){ return net == network; }

	@Override
	public int getExportablePower(INetwork network){ return energy; }

	@Override
	public int exportPower(INetwork network, int amount)
	{
		energy -= amount;
		return amount;
	}

	@Override
	public boolean canImportPower(INetwork network)
	{
		return true;
	}

	@Override
	public int getRequestedPower(INetwork network){ return max - energy; }

	@Override
	public int importPower(INetwork network, int amount)
	{
		energy += amount;
		return amount;
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		MessageHelper.sendMessage(ent, "Energy: " + energy + "/" + max);
		if(net != null)
			MessageHelper.sendMessage(ent, net.toString());
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("en", energy);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		energy = nbt.getInteger("en");
	}

}
