package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.items.MagicScroll;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TargetMobsSymbolicSpell extends AbstractEnchantGemSymbolic
{

	public TargetMobsSymbolicSpell(SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		super(rootRune,center);
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
	public void enchant(Size size, ItemStack is)
	{
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
