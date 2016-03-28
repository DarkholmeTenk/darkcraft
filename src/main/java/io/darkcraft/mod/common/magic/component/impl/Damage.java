package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IDurationComponent;
import io.darkcraft.mod.common.magic.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.component.impl.effects.EffectDamage;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Damage implements IComponent, IDurationComponent, IMagnitudeComponent
{
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/magic/damage.png");

	@Override
	public String id()
	{
		return "damage";
	}

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.destruction;
	}

	@Override
	public double getCost()
	{
		return 8;
	}

	@Override
	public boolean applyToBlock()
	{
		return false;
	}

	@Override
	public boolean applyToEnt()
	{
		return true;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int magnitude, int duration)
	{
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase living = (EntityLivingBase) ent;
		EntityEffectStore ees = EffectHandler.getEffectStore(living);
		ees.addEffect(new EffectDamage(living,caster,magnitude,duration*20));
	}

	@Override
	public int getMinMagnitude()
	{
		return 1;
	}

	@Override
	public int getMaxMagnitude()
	{
		return 40;
	}

	@Override
	public double getCostMag(int magnitude, double oldCost)
	{
		return magnitude * oldCost;
	}

	@Override
	public int getMinDuration()
	{
		return 1;
	}

	@Override
	public int getMaxDuration()
	{
		return 10;
	}

	@Override
	public double getCostDur(int duration, double oldCost)
	{
		return oldCost * duration;
	}

	@Override
	public ResourceLocation getProjectileTexture()
	{
		return tex;
	}

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.damage";}

	@Override
	public ResourceLocation getIcon(){return MagicalRegistry.componentTex;}

	private final UVStore uv = new UVStore(0,0.1,0,0.1);
	@Override
	public UVStore getIconLocation(){return uv;}

}
