package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.magic.systems.enchantment.ArmorEnchantment;
import io.darkcraft.mod.common.magic.systems.enchantment.EnchantmentHelper;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ArmorEnchantSymbolic extends AbstractSpellEnchanterSymbolic
{

	public ArmorEnchantSymbolic(SimpleCoordStore _root, SimpleCoordStore _center)
	{
		super(_root, _center);
	}

	private boolean enchantArmor(EntityPlayer pl, ArmorEnchantment ae)
	{
		ItemStack is = pl.getHeldItem();
		if(is == null) return false;
		Item i = is.getItem();
		if(i instanceof ItemArmor)
		{
			if(EnchantmentHelper.getArmorEnchantment(is) != null)
			{
				MessageHelper.sendMessage(pl, "dc.message.alreadyenchanted");
				return false;
			}
			ae.writeToItem(is);
			return true;
		}
		return false;
	}

	private int getEnchantability(EntityPlayer pl)
	{
		ItemStack is = pl.getHeldItem();
		if(is == null) return 1;
		Item i = is.getItem();
		if(i instanceof ItemArmor)
		{
			return Math.max(1,((ItemArmor) i).getArmorMaterial().getEnchantability());
		}
		return 1;
	}

	@Override
	public boolean enchant(PlayerCaster pc, EntityPlayer pl, GemStand gs)
	{
		Size size = gs.getSoulSize();
		Spell spell = pc.getCurrentSpell();
		ArmorEnchantment ae = EnchantmentHelper.getArmorEnchantment(spell);
		if((size == null) || (ae == null))return false;
		int ench = getEnchantability(pl);
		int maxEnch = (int) (size.getMaxCharge() * ench);
		if(ae.cost > maxEnch)
		{
			MessageHelper.sendMessage(pl, "dc.message.weaksoul");
			return false;
		}
		double cost = spell.getCost(pc);
		if(pc.useMana(cost, true))
		{
			if(enchantArmor(pl, ae))
			{
				pc.useMana(cost, false);
				return true;
			}
		}
		else
			MessageHelper.sendMessage(pl, "dc.message.nomana");
		return false;
	}

}
