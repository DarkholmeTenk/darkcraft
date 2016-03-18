package io.darkcraft.mod.common.network;

import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.MagicEventHandler;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerCasterPacketHandler implements IDataPacketHandler
{
	public static final byte pcDisc = 51;

	@Override
	public void handleData(NBTTagCompound data)
	{
		if(data.hasKey("pln")) handlerServerSide(data);

		if(data.hasKey("dcpc") && ServerHelper.isClient())
			handleClientSide(data);
	}

	private void handlerServerSide(NBTTagCompound data)
	{
		EntityPlayer pl = PlayerHelper.getPlayer(data.getString("pln"));
		if(pl == null) return;
		PlayerCaster plc = Helper.getPlayerCaster(pl);
		if(data.hasKey("rca"))
		{
			MagicEventHandler.i.handleInteract(new PlayerInteractEvent(pl,Action.RIGHT_CLICK_AIR,0,0,0,0,null));
		}
		else if(data.hasKey("curSpellIndex"))
		{
			int i = data.getInteger("curSpellIndex");
			int[] hks = data.getIntArray("hotkeys");
			plc.setCurrentSpell(i);
			plc.setHotkeys(hks);
		}
		else if(data.hasKey("rem"))
			plc.loadNBTData(data);

	}

	@SideOnly(Side.CLIENT)
	private void handleClientSide(NBTTagCompound data)
	{
		EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
		PlayerCaster plc = Helper.getPlayerCaster(pl);
		plc.loadNBTData(data);
	}

}
