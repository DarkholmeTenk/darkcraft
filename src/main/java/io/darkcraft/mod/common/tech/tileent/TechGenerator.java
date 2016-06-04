package io.darkcraft.mod.common.tech.tileent;

import io.darkcraft.api.tech.network.INetwork;
import io.darkcraft.api.tech.network.INetworkTE;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;
import io.darkcraft.mod.common.tech.network.NetworkHelper;
import io.darkcraft.mod.common.tech.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class TechGenerator extends AbstractMFTileEntity implements INetworkTE, IActivatable
{
	private INetwork net;

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
	public void setNetwork(INetwork network){ net = network; }

	@Override
	public boolean canExportPower(INetwork network){ return network == net; }

	@Override
	public int getExportablePower(INetwork network){ return 1; }

	@Override
	public int exportPower(INetwork network, int amount){ return Math.min(amount, 1); }

	@Override
	public boolean canImportPower(INetwork network){ return false; }

	@Override
	public int getRequestedPower(INetwork network){ return 0; }

	@Override
	public int importPower(INetwork network, int amount){ return 0; }

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if(net != null)
		{
			MessageHelper.sendMessage(ent, net.toString());
		}
		return false;
	}

}
