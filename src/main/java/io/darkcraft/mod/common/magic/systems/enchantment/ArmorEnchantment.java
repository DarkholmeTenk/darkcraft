package io.darkcraft.mod.common.magic.systems.enchantment;

import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ArmorEnchantment extends Enchantment
{
	public final ComponentInstance[] components;
	public final String name;
	public final double cost;
	private String player;

	public ArmorEnchantment(String _name, ComponentInstance[] comps, double _cost)
	{
		name = _name;
		components = comps;
		cost = _cost;
	}

	@Override
	public double getCost()
	{
		return cost;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void apply(EntityPlayer pl)
	{
		player = PlayerHelper.getUsername(pl);
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		for(ComponentInstance ci : components)
			ci.component.apply(pc, pl, ci.magnitude, -1, ci.config);
	}

	public void remove(EntityPlayer pl)
	{
		player = null;
		for(ComponentInstance ci : components)
			((IDurationComponent)ci.component).remove(pl);
	}

	public boolean isApplied(EntityPlayer pl)
	{
		if(player == null) return false;
		return PlayerHelper.getUsername(pl).equals(player);
	}

	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", name);
		nbt.setInteger("comps", components.length);
		nbt.setDouble("cost", cost);
		for(int i = 0; i < components.length; i++)
			components[i].writeToNBT(nbt, "comp"+i);
		return nbt;
	}

	public void writeToItem(ItemStack is)
	{
		if(is.stackTagCompound == null)
			is.stackTagCompound = new NBTTagCompound();
		is.stackTagCompound.setTag("dcArmorEnchant", writeToNBT());
	}

	public static ArmorEnchantment readFromNBT(NBTTagCompound nbt)
	{
		String name = nbt.getString("name");
		int cl = nbt.getInteger("comps");
		double co = nbt.getDouble("cost");
		ComponentInstance[] comps = new ComponentInstance[cl];
		for(int i = 0; i < cl; i++)
			comps[i] = ComponentInstance.readFromNBT(nbt, "comp"+i);
		return new ArmorEnchantment(name,comps,co);
	}

	public static ArmorEnchantment readFromItem(ItemStack is)
	{
		if((is.stackTagCompound == null) || !is.stackTagCompound.hasKey("dcArmorEnchant")) return null;
		return readFromNBT(is.stackTagCompound.getCompoundTag("dcArmorEnchant"));
	}

}
