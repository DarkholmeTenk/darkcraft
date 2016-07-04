package io.darkcraft.mod.common.magic.systems.component.impl;

import java.util.ArrayList;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DigSilk extends Dig
{
	@Override
	public String id(){ return "digsilk"; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.digsilk";}

	@Override
	public double getCost()
	{
		return super.getCost() * MagicConfig.fortuneMult;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block b, SimpleCoordStore bp, EntityPlayer pl)
	{
		if(b.canSilkHarvest(bp.getWorldObj(), pl, bp.x, bp.y, bp.z, bp.getMetadata()))
		{
			ArrayList<ItemStack> items = new ArrayList();
			items.add(new ItemStack(b,1,bp.getMetadata()));
			return items;
		}
		return super.getDrops(b, bp, pl);
	}

	private final UVStore uv = new UVStore(0.0,0.1,0.2,0.3);
	@Override
	public UVStore getIconLocation(){return uv;}
}
