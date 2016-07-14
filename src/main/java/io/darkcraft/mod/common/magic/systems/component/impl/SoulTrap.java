package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.effects.EffectSoulTrap;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class SoulTrap implements IComponent, IDurationComponent
{

	@Override
	public String id()
	{
		return "soultrap";
	}

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.soultrap";}

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uv = new UVStore(0.3,0.4,0.3,0.4);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.mysticism; }

	@Override
	public double getCost(){ return 4; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase living = (EntityLivingBase) ent;
		EntityEffectStore ees = EffectHandler.getEffectStore(living);
		ees.addEffect(new EffectSoulTrap(caster,living,magnitude,duration*20));
	}

	@Override
	public void remove(EntityLivingBase ent)
	{
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		if(ees != null)
			ees.remove("darkcraft.soultrap");
	}

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public boolean applyToBlock(){ return false; }

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.4,0.5,0,0.1)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public int getMinDuration()
	{
		return 1;
	}

	@Override
	public int getMaxDuration()
	{
		return 300;
	}

	@Override
	public double getCostDur(int duration, double oldCost)
	{
		return duration * oldCost;
	}

}
