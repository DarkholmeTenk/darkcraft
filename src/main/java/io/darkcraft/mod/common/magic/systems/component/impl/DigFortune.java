package io.darkcraft.mod.common.magic.systems.component.impl;

import java.util.ArrayList;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DigFortune extends Dig
{
	@Override
	public String id(){ return "digfortune"; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.digfortune";}

	@Override
	public double getCost()
	{
		return super.getCost() * MagicConfig.fortuneMult;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block b, SimpleCoordStore bp, EntityPlayer pl)
	{
		return b.getDrops(bp.getWorldObj(), bp.x, bp.y, bp.z, bp.getMetadata(), MagicConfig.fortuneLevel);
	}

	private final UVStore uv = new UVStore(0.0,0.1,0.3,0.4);
	@Override
	public UVStore getIconLocation(){return uv;}
}
