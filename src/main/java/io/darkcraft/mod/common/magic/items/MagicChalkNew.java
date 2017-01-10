package io.darkcraft.mod.common.magic.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;

public class MagicChalkNew extends AbstractItem
{
	public MagicChalkNew()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("MagicChalkNew");
		setSubNames(ChalkType.getNames());
		setMaxStackSize(1);
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer pl)
	{
		if(ServerHelper.isClient(w) && pl.isSneaking())
		{
			pl.openGui(DarkcraftMod.i, 1400, w, 0, 0, 0);
		}
		return is;
	}

}
