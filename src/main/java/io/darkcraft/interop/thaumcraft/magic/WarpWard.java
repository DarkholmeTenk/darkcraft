package io.darkcraft.interop.thaumcraft.magic;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class WarpWard implements IComponent, IDurationComponent
{

	@Override
	public int getMinDuration()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxDuration()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCostDur(int duration, double oldCost)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String id()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnlocalisedName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceLocation getIcon()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UVStore getIconLocation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISkill getMainSkill()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean applyToEnt()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean applyToBlock()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResourceLocation getProjectileTexture()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UVStore getProjectileLocation(int frame)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
