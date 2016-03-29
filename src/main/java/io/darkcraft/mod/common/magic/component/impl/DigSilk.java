package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
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
	public void apply(ICaster caster, SimpleCoordStore bp, int magnitude, int duration)
	{
		if(bp == null) return;
		Block b = bp.getBlock();
		if(b == null) return;
		if(canBreak(magnitude, b.getBlockHardness(bp.getWorldObj(), bp.x, bp.y, bp.z)))
		{
			EntityPlayer pl = null;
			if(caster instanceof PlayerCaster) pl = ((PlayerCaster) caster).getCaster();
			if(b.canSilkHarvest(bp.getWorldObj(), pl, bp.x, bp.y, bp.z, bp.getMetadata()))
			{
				ItemStack is = new ItemStack(b, 1, bp.getMetadata());
				bp.setToAir();
				if(ServerHelper.isServer())
					WorldHelper.dropItemStack(is, bp.getCenter());
			}
			else
				super.apply(caster, bp, magnitude, duration);
		}
	}

	private final UVStore uv = new UVStore(0.0,0.1,0.2,0.3);
	@Override
	public UVStore getIconLocation(){return uv;}
}
