package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IDurationComponent;
import io.darkcraft.mod.common.magic.component.impl.effects.EffectFly;
import io.darkcraft.mod.common.registries.MagicalRegistry;
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
		return 100;
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
	public double getCostDur(int duration, double oldCost)
	{
		return oldCost * Math.pow(duration,1.05);
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
			ees.addEffect(new EffectFly(caster,ent,magnitude,duration*20));
	}

	@Override
	public boolean applyToEnt(){return true;}

	@Override
	public boolean applyToBlock(){return false;}

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.125,0.1875,0,0.0625), new UVStore(0,0.0625,0.0625,0.125)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.fly";}

	@Override
	public ResourceLocation getIcon(){return MagicalRegistry.componentTex;}

	private final UVStore uv = new UVStore(0.2,0.3,0.0,0.1);
	@Override
	public UVStore getIconLocation(){return uv;}

}
