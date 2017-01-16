package io.darkcraft.mod.common.magic.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.item.MagicChalkRenderer;
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

	public static double getISDamage(ItemStack is)
	{
		if((is == null) || !(is.getItem() instanceof MagicChalkNew)) return 0;
		if(is.stackTagCompound == null) return 0;
		if(!is.stackTagCompound.hasKey("dam")) is.stackTagCompound.setDouble("dam", 0);
		return is.stackTagCompound.getDouble("dam");
	}

	@Override
	public IItemRenderer getRenderer()
	{
		return new MagicChalkRenderer();
	}
}
