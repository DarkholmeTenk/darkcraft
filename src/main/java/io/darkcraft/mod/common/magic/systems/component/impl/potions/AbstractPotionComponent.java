package io.darkcraft.mod.common.magic.systems.component.impl.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;

import skillapi.api.implement.ISkill;

public abstract class AbstractPotionComponent implements IComponent, IDurationComponent
{
	private final String id;
	private final String unlocal;
	private final int maxDuration;
	private final ISkill skill;

	private final ResourceLocation rl;
	private final UVStore uv;

	public AbstractPotionComponent(String id, String unlocal, int maxDuration, ISkill skill, UVStore uv)
	{
		this(id, unlocal, maxDuration, skill, MagicalRegistry.componentTex, uv);
	}

	public AbstractPotionComponent(String id, String unlocal, int maxDuration, ISkill skill, ResourceLocation rl, UVStore uv)
	{
		this.id = id;
		this.unlocal = unlocal;
		this.maxDuration = maxDuration;
		this.skill = skill;
		this.rl = rl;
		this.uv = uv;
	}

	@Override
	public int getMinDuration()
	{
		return 5;
	}

	@Override
	public int getMaxDuration()
	{
		return maxDuration;
	}

	@Override
	public double getCostDur(int duration, double oldCost)
	{
		return oldCost * Math.pow(duration,1.05);
	}

	@Override
	public String id(){ return id; }

	@Override
	public String getUnlocalisedName(){ return unlocal; }

	@Override
	public ResourceLocation getIcon(){ return rl; }

	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill(){ return skill; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(ent instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase) ent;
			PotionEffect effect = getPotionEffect(magnitude, duration);
			if(effect != null)
				living.addPotionEffect(effect);
		}
	}

	public abstract PotionEffect getPotionEffect(int mag, int dur);

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public boolean applyToBlock(){ return false; }

}
