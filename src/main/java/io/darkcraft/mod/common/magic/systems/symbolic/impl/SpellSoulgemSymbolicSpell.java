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

public class SpellSoulgemSymbolicSpell extends AbstractEnchantGemSymbolic
{
	private final String soulspellID;
	private final INBTWriter nbtWriter;

	public SpellSoulgemSymbolicSpell(SimpleCoordStore rootRune, SimpleCoordStore center, String soulspell)
	{
		this(rootRune, center, soulspell, null);
	}

	public SpellSoulgemSymbolicSpell(SimpleCoordStore rootRune, SimpleCoordStore center, String soulspell, INBTWriter nbtW)
	{
		super(rootRune,center);
		soulspellID = soulspell;
		nbtWriter = nbtW;
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
					ei.setDead();
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
			SoulGem.setSoulSpell(is, soulspellID);
			NBTTagCompound snbt = new NBTTagCompound();
			sp.writeToNBT(snbt);
			NBTTagCompound nbt = SoulGem.getSoulSpellData(is);
			nbt.setTag("spell", snbt);
			writeExtraNBT(nbt);
		}
	}

	public void writeExtraNBT(NBTTagCompound nbt)
	{
		if(nbtWriter != null)
			nbtWriter.writeToNBT(nbt);
	}
}
