package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.component.IDurationComponent;
import io.darkcraft.mod.common.magic.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.component.impl.effects.EffectRestoreHealth;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class RestoreHealth implements IComponent, IDurationComponent, IMagnitudeComponent
{
	@Override
	public String id(){ return "restore"; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.restoration; }

	@Override
	public double getCost(){ return 10; }

	@Override
	public boolean applyToBlock(){ return false; }

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int magnitude, int duration){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase living = (EntityLivingBase) ent;
		EntityEffectStore ees = EffectHandler.getEffectStore(living);
		ees.addEffect(new EffectRestoreHealth(caster,living,magnitude,duration*20));
	}

	@Override
	public int getMinMagnitude(){ return 1; }

	@Override
	public int getMaxMagnitude(){ return 40; }

	@Override
	public double getCostMag(int magnitude, double oldCost){ return Math.pow(magnitude,1.25) * oldCost; }

	@Override
	public int getMinDuration(){ return 1; }

	@Override
	public int getMaxDuration(){ return 60; }

	@Override
	public double getCostDur(int duration, double oldCost){ return oldCost * duration; }

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.3,0.4,0,0.1)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.restore"; }

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uv = new UVStore(0.2,0.3,0.1,0.2);
	@Override
	public UVStore getIconLocation(){ return uv; }

}
