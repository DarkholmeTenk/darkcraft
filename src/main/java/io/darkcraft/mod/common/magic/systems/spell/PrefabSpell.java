package io.darkcraft.mod.common.magic.systems.spell;

import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.nbt.NBTTagCompound;

public class PrefabSpell extends Spell
{
	private final String id;
	private final double cost;

	public PrefabSpell(String _id, String _name, ComponentInstance[] componentInstances, CastType castType, double _cost)
	{
		super(_name, componentInstances, castType);
		id = _id;
		cost = _cost;
	}

	@Override
	public double getCost(ICaster caster)
	{
		return cost;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("pfs", id);
	}

}
