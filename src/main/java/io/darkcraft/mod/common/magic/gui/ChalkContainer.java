package io.darkcraft.mod.common.magic.gui;

import io.darkcraft.mod.common.magic.items.MagicChalk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ChalkContainer extends Container
{
	public String chalk;
	public EntityPlayer pl;

	private String getChalkText(EntityPlayer pl)
	{
		ItemStack is = pl.getHeldItem();
		if((is == null) || !(is.getItem() instanceof MagicChalk) || (is.stackTagCompound == null)) return "";
		return MagicChalk.getString(is);
	}

	public ChalkContainer(EntityPlayer pl)
	{
		chalk = getChalkText(pl);
		this.pl = pl;
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

}
