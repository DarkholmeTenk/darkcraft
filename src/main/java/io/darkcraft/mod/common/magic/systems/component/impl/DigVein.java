package io.darkcraft.mod.common.magic.systems.component.impl;

import java.util.ArrayList;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.DelayedItemHandler;
import io.darkcraft.darkcore.mod.helpers.BlockIterator;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.systems.component.INoAreaComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DigVein extends Dig implements INoAreaComponent
{
	@Override
	public String id(){ return "digvein"; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.digvein";}

	@Override
	public double getCost()
	{
		return super.getCost();
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block b, SimpleCoordStore bp, EntityPlayer pl)
	{
		return super.getDrops(b, bp, pl);
	}

	public void digBlock(EntityPlayer pl, int magnitude, Block b, SimpleCoordStore bp)
	{
		if((bp == null) || (b == null)) return;
		ArrayList<ItemStack> drops = getDrops(b,bp,pl);
		bp.setToAir();
		if((drops != null) && ServerHelper.isServer())
		{
			SimpleDoubleCoordStore center = bp.getCenter();
			if(pl != null)
				DelayedItemHandler.addItemDrop(pl, drops);
			else
				for(ItemStack is : drops)
					WorldHelper.dropItemStack(is, center);
		}
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore bp, int side, int magnitude, int duration, int config)
	{
		if(bp == null) return;
		Block b = bp.getBlock();
		if(b == null) return;
		if(canBreak(magnitude, b.getBlockHardness(bp.getWorldObj(), bp.x, bp.y, bp.z)))
		{
			double cost = getCostMag(magnitude, getCost()) / 2;
			EntityPlayer pl = caster instanceof PlayerCaster ? ((PlayerCaster)caster).getCaster() : null;
			BlockIterator bi = new BlockIterator(bp, BlockIterator.sameIncMeta, true, 100);
			for(int i = 0; i < 256; i++)
			{
				SimpleCoordStore scs = bi.next();
				if(scs == null) return;
				if(caster.useMana(cost, false))
					digBlock(pl, magnitude, b, scs);
				else
					return;
			}
		}
	}

	private final UVStore uv = new UVStore(0.0,0.1,0.4,0.5);
	@Override
	public UVStore getIconLocation(){return uv;}
}
