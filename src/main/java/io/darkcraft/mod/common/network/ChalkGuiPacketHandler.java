package io.darkcraft.mod.common.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.magic.items.MagicChalk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ChalkGuiPacketHandler implements IDataPacketHandler
{
	public static final String disc = "dc.chalk";

	@SideOnly(Side.CLIENT)
	public static void updateChalk(EntityPlayer pl, String newChalk)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("username", PlayerHelper.getUsername(pl));
		nbt.setString("chalk", newChalk);
		DataPacket dp = new DataPacket(nbt,disc);
		DarkcoreMod.networkChannel.sendToServer(dp);
	}

	@Override
	public void handleData(NBTTagCompound data)
	{
		 EntityPlayer pl = PlayerHelper.getPlayer(data.getString("username"));
		 if(pl == null) return;
		 ItemStack is = pl.getHeldItem();
		 if((is != null) && (is.getItem() instanceof MagicChalk))
		 {
			 MagicChalk.setString(is, data.getString("chalk"));
			 pl.inventory.markDirty();
		 }
	}

}
