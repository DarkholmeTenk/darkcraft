package io.darkcraft.mod.common.magic.systems.enchantment;

import java.util.WeakHashMap;

import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentHelper
{
	protected static WeakHashMap<ItemStack, ArmorEnchantment> armorEnchants = new WeakHashMap();

	public static Enchantment getEnchantment(ItemStack is)
	{
		return getArmorEnchantment(is);
	}

	public static ArmorEnchantment getArmorEnchantment(ItemStack is)
	{
		if(is == null) return null;
		if(armorEnchants.containsKey(is)) return armorEnchants.get(is);
		Item i = is.getItem();
		if(i instanceof ItemArmor)
		{
			ArmorEnchantment ae = ArmorEnchantment.readFromItem(is);
			if(ae == null) return null;
			armorEnchants.put(is, ae);
			return ae;
		}
		return null;
	}

	public static ArmorEnchantment getArmorEnchantment(Spell spell)
	{
		if((spell == null) || (spell.type != CastType.SELF)) return null;
		double cost = 0;
		ComponentInstance[] compInstances = new ComponentInstance[spell.components.length];
		for(int i = 0; i < compInstances.length; i++)
		{
			double ciCost = 0;
			ComponentInstance ci = spell.components[i];
			IComponent c = ci.component;
			int mag = ci.magnitude;
			if(!(c instanceof IDurationComponent)) return null;
			if(ci.duration < 600) return null;
			ciCost = c.getCost();
			if(c instanceof IMagnitudeComponent)
				ciCost = ((IMagnitudeComponent) c).getCostMag(mag, ciCost);
			compInstances[i] = new ComponentInstance(c, mag, -1);
			compInstances[i].config = ci.config;
			cost += ciCost;
		}
		cost *= MagicConfig.armorEnchCostMult;
		return new ArmorEnchantment(spell.name, compInstances, cost);
	}
}
