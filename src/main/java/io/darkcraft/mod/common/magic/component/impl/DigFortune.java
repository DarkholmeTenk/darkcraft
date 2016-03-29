package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicConfig;

import java.util.ArrayList;

import net.minecraft.block.Block;
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
	public void apply(ICaster caster, SimpleCoordStore bp, int magnitude, int duration)
	{
		if(bp == null) return;
		Block b = bp.getBlock();
		if(b == null) return;
		if(canBreak(magnitude, b.getBlockHardness(bp.getWorldObj(), bp.x, bp.y, bp.z)))
		{
			ArrayList<ItemStack> drops = b.getDrops(bp.getWorldObj(), bp.x, bp.y, bp.z, bp.getMetadata(), MagicConfig.fortuneLevel);
			bp.setToAir();
			if((drops != null) && ServerHelper.isServer())
			{
				SimpleDoubleCoordStore center = bp.getCenter();
				for(ItemStack is : drops)
				{
					WorldHelper.dropItemStack(is, center);
				}
			}
		}
	}

	private final UVStore uv = new UVStore(0.0,0.1,0.3,0.4);
	@Override
	public UVStore getIconLocation(){return uv;}
}
