package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
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
		return SkillRegistry.destruction;
	}

	@Override
	public double getCost()
	{
		return 0;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config)
	{
		if((caster == null) || (blockPos == null)) return;
		String s = "T:"+caster.toString()+","+blockPos.toString();
		if(caster instanceof EntityCaster)
			ServerHelper.sendString((EntityPlayer)(((EntityCaster)caster).getCaster()), s);
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
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
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.damage.getProjectileTexture(); }

	@Override
	public UVStore getProjectileLocation(int f){ return MagicalRegistry.damage.getProjectileLocation(f); }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.test";}

	@Override
	public ResourceLocation getIcon(){return MagicalRegistry.componentTex;}

	private final UVStore uv = new UVStore(0.1,0.2,0,0.1);
	@Override
	public UVStore getIconLocation(){return uv;}
}
