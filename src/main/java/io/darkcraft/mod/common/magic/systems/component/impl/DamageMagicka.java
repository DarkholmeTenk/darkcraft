package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.effects.EffectDamageMagicka;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import skillapi.api.implement.ISkill;

public class DamageMagicka extends Damage
{
	@Override
	public String id(){ return "damagemagicka"; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.destruction; }

	@Override
	public double getCost(){ return 5; }

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase living = (EntityLivingBase) ent;
		if(Helper.isCaster(caster, living)) return;
		EntityEffectStore ees = EffectHandler.getEffectStore(living);
		ees.addEffect(new EffectDamageMagicka(caster,living,magnitude,duration*20));
	}

	@Override
	public int getMinMagnitude(){ return 1; }

	@Override
	public int getMaxMagnitude(){ return 10000; }

	@Override
	public double getCostMag(int magnitude, double oldCost){ return Math.pow(magnitude,0.5) * oldCost; }

	@Override
	public int getMinDuration(){ return 1; }

	@Override
	public int getMaxDuration(){ return 30; }

	@Override
	public double getCostDur(int duration, double oldCost){ return oldCost * duration; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.damagemagicka";}


	private final UVStore uv = new UVStore(0.1,0.2,0.1,0.2);
	@Override
	public UVStore getIconLocation(){return uv;}

}
