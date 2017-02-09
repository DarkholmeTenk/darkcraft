package io.darkcraft.mod.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.interfaces.IDataPacketHandler;
import io.darkcraft.darkcore.mod.network.DataPacket;
import io.darkcraft.mod.common.magic.items.MagicChalkNew;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChalkGuiPacketHandler implements IDataPacketHandler
{
	public static final String disc = "dc.chalk";

	@SideOnly(Side.CLIENT)
	public static void updateChalk(EntityPlayer pl, String[] newChalk)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("username", PlayerHelper.getUsername(pl));
		MagicChalkNew.setStrings(nbt, newChalk);
		DataPacket dp = new DataPacket(nbt,disc);
		DarkcoreMod.networkChannel.sendToServer(dp);
	}

	@Override
	public void handleData(NBTTagCompound data)
	{
		 EntityPlayer pl = PlayerHelper.getPlayer(data.getString("username"));
		 if(pl == null) return;
		 ItemStack is = pl.getHeldItem();
		 if((is != null) && (is.getItem() instanceof MagicChalkNew))
		 {
			 MagicChalkNew.setStrings(is, MagicChalkNew.getStrings(data));
			 pl.inventory.markDirty();
		 }
	}

}
