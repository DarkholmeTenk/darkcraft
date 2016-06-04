package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class ConjureLight implements IComponent
{

	@Override
	public String id(){ return "conjurelight"; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.conjurelight"; }

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
	public ISkill getMainSkill(){ return SkillRegistry.conjuration; }

	@Override
	public double getCost(){ return 5; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration)
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
