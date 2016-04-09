package io.darkcraft.mod.common.magic.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.impl.effects.EffectRestoreHunger;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import skillapi.api.implement.ISkill;

public class RestoreHunger extends RestoreHealth
{
	@Override
	public String id(){ return "restorehunger"; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.restoration; }

	@Override
	public double getCost(){ return 7; }

	@Override
	public boolean applyToBlock(){ return false; }

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase living = (EntityLivingBase) ent;
		EntityEffectStore ees = EffectHandler.getEffectStore(living);
		ees.addEffect(new EffectRestoreHunger(caster,living,magnitude,duration*20));
	}

	@Override
	public int getMinMagnitude(){ return 1; }

	@Override
	public int getMaxMagnitude(){ return 4; }

	@Override
	public double getCostMag(int magnitude, double oldCost){ return Math.pow(magnitude,1.25) * oldCost; }

	@Override
	public int getMinDuration(){ return 1; }

	@Override
	public int getMaxDuration(){ return 120; }

	@Override
	public double getCostDur(int duration, double oldCost){ return oldCost * duration; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.restorehunger"; }

	private final UVStore uv = new UVStore(0.2,0.3,0.2,0.3);
	@Override
	public UVStore getIconLocation(){ return uv; }

}
