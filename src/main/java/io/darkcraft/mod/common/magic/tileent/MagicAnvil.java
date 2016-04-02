package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.spell.ISpellable;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MagicAnvil extends AbstractTileEntity implements ISpellable, IActivatablePrecise
{
	private ItemStack leftItem;
	private ItemStack centerItem;
	private ItemStack rightItem;

	public ItemStack getLeftItem()
	{
		return leftItem;
	}

	public ItemStack getCenterItem()
	{
		return centerItem;
	}

	public ItemStack getRightItem()
	{
		return rightItem;
	}

	public boolean isProcessing()
	{
		return false;
	}

	@Override
	public boolean spellHit(SimpleCoordStore block, Spell spell, ICaster caster)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean activate(EntityPlayer ent, int side, float x, float y, float z)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
