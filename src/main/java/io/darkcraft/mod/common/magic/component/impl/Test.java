package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Test implements IComponent
{

	@Override
	public String id()
	{
		return "Test";
	}

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.alteration;
	}

	@Override
	public double getCost()
	{
		return 0;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int magnitude, int duration)
	{
		if((caster == null) || (blockPos == null)) return;
		String s = "T:"+caster.toString()+","+blockPos.toString();
		if(caster instanceof EntityCaster)
			ServerHelper.sendString((EntityPlayer)(((EntityCaster)caster).getCaster()), s);
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration)
	{
		if((caster == null) || (ent == null)) return;
		String s = "T:"+caster.toString()+","+ent.toString();
		if(caster instanceof EntityCaster)
			ServerHelper.sendString((EntityPlayer)(((EntityCaster)caster).getCaster()), s);
	}

	@Override
	public boolean applyToEnt()
	{
		return true;
	}

	@Override
	public boolean applyToBlock()
	{
		return true;
	}

	@Override
	public ResourceLocation getProjectileTexture()
	{
		// TODO Auto-generated method stub
		return null;
	}

}