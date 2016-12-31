package io.darkcraft.mod.common.magic.systems.component.impl;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.abstracts.effects.AbstractEffect;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

import skillapi.api.implement.ISkill;

public class Dispel implements IComponent
{

	@Override
	public String id()
	{
		return "dispel";
	}

	@Override
	public String getUnlocalisedName()
	{
		return "darkcraft.component.dispel";
	}

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uv = new UVStore(0.6,0.7,0.4,0.5);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.mysticism; }

	@Override
	public double getCost()
	{
		return 300;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(ServerHelper.isClient(ent)) return;
		EntityEffectStore ees = EffectHandler.getEffectStore(ent);
		Iterator<AbstractEffect> effectIter = ees.getEffects().iterator();
		while(effectIter.hasNext())
		{
			AbstractEffect effect = effectIter.next();
			if(effect.dispellable())
			{
				effect.effectRemoved();
				effectIter.remove();
			}
		}
		ees.sendUpdate();

		if(ent instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase) ent;
			living.curePotionEffects(new ItemStack(Items.milk_bucket));
		}
	}

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public boolean applyToBlock(){ return false; }

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.2,0.3,0,0.1)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

}
