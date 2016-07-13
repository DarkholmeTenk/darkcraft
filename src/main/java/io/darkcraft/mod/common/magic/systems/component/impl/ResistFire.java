package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.effects.EffectResistFire;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class ResistFire implements IComponent, IMagnitudeComponent, IDurationComponent
{

	@Override
	public String id()
	{
		return "featherfall";
	}

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.mysticism;
	}

	@Override
	public double getCost()
	{
		return 3;
	}

	@Override
	public int getMinDuration()
	{
		return 5;
	}

	@Override
	public int getMaxDuration()
	{
		return 3600;
	}

	@Override
	public double getCostDur(int duration, double oldCost)
	{
		return oldCost * Math.pow(duration,1.05);
	}

	@Override
	public int getMinMagnitude()
	{
		return 1;
	}

	@Override
	public int getMaxMagnitude()
	{
		return 5;
	}

	@Override
	public double getCostMag(int magnitude, double oldCost)
	{
		return oldCost * magnitude;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity entity, int magnitude, int duration, int config)
	{
		if(!(entity instanceof EntityLivingBase))return;
		EntityLivingBase ent = (EntityLivingBase) entity;
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		if(ees != null)
			ees.addEffect(new EffectResistFire(caster,ent,magnitude,duration*20));
	}

	@Override
	public boolean applyToEnt(){return true;}

	@Override
	public boolean applyToBlock(){return false;}

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.2,0.3,0,0.1)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.resistfire";}

	@Override
	public ResourceLocation getIcon(){return MagicalRegistry.componentTex;}

	private final UVStore uv = new UVStore(0.5,0.6,0.4,0.5);
	@Override
	public UVStore getIconLocation(){return uv;}

}
