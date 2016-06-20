package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicSymbol;
import io.darkcraft.mod.common.magic.items.MagicScroll;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TargetMobsSymbolicSpell implements ISymbolicSpell
{
	private final SimpleCoordStore root;
	private final SimpleCoordStore center;

	public TargetMobsSymbolicSpell(SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		root = rootRune;
		this.center = center;
	}

	private Spell getNearbySpell()
	{
		List list = center.getWorldObj().getEntitiesWithinAABB(EntityItem.class, center.getCenter().getAABB(1.5));
		for(Object o : list)
		{
			if(!(o instanceof EntityItem)) continue;
			EntityItem ei = (EntityItem) o;
			ItemStack is = ei.getEntityItem();
			if(is.getItem() instanceof MagicScroll)
			{
				Spell sp = MagicScroll.getSpell(is);
				if(sp != null)
				{
					ei.isDead = true;
					return sp;
				}
			}
		}
		return null;
	}

	@Override
	public void tick()
	{
		TileEntity te = center.getTileEntity();
		if(te instanceof GemStand)
		{
			GemStand gs = (GemStand) te;
			if(gs.isGemFull())
			{
				ItemStack is = gs.getIS();
				Spell sp = getNearbySpell();
				if(sp != null)
				{
					SoulGem.setSoulSpell(is, "dc.targetmobs");
					NBTTagCompound snbt = new NBTTagCompound();
					sp.writeToNBT(snbt);
					SoulGem.getSoulSpellData(is).setTag("spell", snbt);
				}
			}
		}
		removeCircle();
	}

	private void removeCircle()
	{
		TileEntity te = root.getTileEntity();
		if(te instanceof MagicSymbol)
		{
			((MagicSymbol)te).clearCircle();
		}
	}

	@Override
	public void cancel()
	{
	}

	@Override
	public void read(NBTTagCompound nbt)
	{
	}

	@Override
	public void write(NBTTagCompound nbt)
	{
	}

	@Override
	public void readTrans(NBTTagCompound nbt)
	{
	}

	@Override
	public void writeTrans(NBTTagCompound nbt)
	{
	}

}
