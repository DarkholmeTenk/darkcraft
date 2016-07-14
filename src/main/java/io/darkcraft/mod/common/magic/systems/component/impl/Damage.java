package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.effects.EffectDamage;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Damage implements IComponent, IDurationComponent, IMagnitudeComponent
{

	@Override
	public String id(){ return "damage"; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.destruction; }

	@Override
	public double getCost(){ return 8; }

	@Override
	public boolean applyToBlock(){ return false; }

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase living = (EntityLivingBase) ent;
		EntityEffectStore ees = EffectHandler.getEffectStore(living);
		ees.addEffect(new EffectDamage(caster,living,magnitude,duration*20));
	}

	@Override
	public void remove(EntityLivingBase ent)
	{
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		if(ees != null)
			ees.remove("darkcraft.damage");
	}

	@Override
	public int getMinMagnitude(){ return 1; }

	@Override
	public int getMaxMagnitude(){ return 40; }

	@Override
	public double getCostMag(int magnitude, double oldCost){ return magnitude * oldCost; }

	@Override
	public int getMinDuration(){ return 1; }

	@Override
	public int getMaxDuration(){ return 30; }

	@Override
	public double getCostDur(int duration, double oldCost){ return oldCost * duration; }

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0,0.1,0,0.1), new UVStore(0,0.1,0.1,0.2)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.damage"; }

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uv = new UVStore(0,0.1,0,0.1);
	@Override
	public UVStore getIconLocation(){ return uv; }

}
