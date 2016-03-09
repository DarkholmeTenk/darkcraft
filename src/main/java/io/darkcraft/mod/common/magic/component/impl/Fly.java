package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IDurationComponent;
import io.darkcraft.mod.common.magic.component.impl.effects.EffectFly;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Fly implements IComponent, IDurationComponent
{

	@Override
	public String id()
	{
		return "fly";
	}

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.alteration;
	}

	@Override
	public double getCost()
	{
		return 10;
	}

	@Override
	public int getMinDuration()
	{
		return 5;
	}

	@Override
	public int getMaxDuration()
	{
		return 120;
	}

	@Override
	public double getCostDur(double duration, double oldCost)
	{
		return oldCost * (duration - 4);
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int magnitude, int duration){}

	@Override
	public void apply(ICaster caster, Entity entity, int magnitude, int duration)
	{
		if(!(entity instanceof EntityLivingBase))return;
		EntityLivingBase ent = (EntityLivingBase) entity;
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		if(ees != null)
			ees.addEffect(new EffectFly(caster,ent,magnitude,duration));
	}

	@Override
	public boolean applyToEnt(){return true;}

	@Override
	public boolean applyToBlock(){return false;}

	@Override
	public ResourceLocation getProjectileTexture()
	{
		return null;
	}

}
