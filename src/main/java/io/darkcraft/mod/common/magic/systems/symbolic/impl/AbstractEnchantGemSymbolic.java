package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicSymbol;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractEnchantGemSymbolic implements ISymbolicSpell
{
	public final SimpleCoordStore root;
	public final SimpleCoordStore center;

	public AbstractEnchantGemSymbolic(SimpleCoordStore rootRune, SimpleCoordStore _center)
	{
		root = rootRune;
		center = _center;
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
				enchant(gs.getSoulSize(), is);
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

	public abstract void enchant(Size soulSize, ItemStack is);

	@Override
	public void cancel() { }

	@Override
	public void read(NBTTagCompound nbt) { }

	@Override
	public void write(NBTTagCompound nbt) { }

	@Override
	public void readTrans(NBTTagCompound nbt) { }

	@Override
	public void writeTrans(NBTTagCompound nbt) { }

}
