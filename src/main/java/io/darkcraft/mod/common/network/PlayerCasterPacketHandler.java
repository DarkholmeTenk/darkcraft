package io.darkcraft.mod.common.network;

import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerCasterPacketHandler implements IDataPacketHandler
{
	public static final byte pcDisc = 51;

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(ServerHelper.isServer()) handlerServerSide(data);
		EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
		PlayerCaster plc = Helper.getPlayerCaster(pl);
		if(plc == null)
		{
			System.err.println("ERRNOCASTERONPACKET!");
			return;
		}
		plc.loadNBTData(data);
	}

	private void handlerServerSide(NBTTagCompound data)
	{
		if(!data.hasKey("pln")) return;
		EntityPlayer pl = PlayerHelper.getPlayer(data.getString("pln"));
		if(pl == null) return;
		PlayerCaster plc = Helper.getPlayerCaster(pl);
		if(data.hasKey("curSpellIndex"))
		{
			int i = data.getInteger("curSpellIndex");
			plc.setCurrentSpell(i);
		}

	}

}
