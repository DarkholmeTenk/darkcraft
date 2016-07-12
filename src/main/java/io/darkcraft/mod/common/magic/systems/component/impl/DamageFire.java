package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.RecipeHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.effects.EffectDamageFire;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import skillapi.api.implement.ISkill;

public class DamageFire extends Damage
{
	@Override
	public String id(){ return "damagefire"; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.destruction; }

	@Override
	public double getCost(){ return 3; }

	@Override
	public boolean applyToBlock(){ return true; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config)
	{
		if((magnitude * duration) < 20) return;
		Block b = blockPos.getBlock();
		int m = blockPos.getMetadata();
		Pair<Block, Integer> result = RecipeHelper.getSmeltResult(b, m);
		if(result != null)
			blockPos.setBlock(result.a, result.b, 3);
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		ent.setFire(duration);
		EntityLivingBase living = (EntityLivingBase) ent;
		if(Helper.isCaster(caster,living)) return;
		EntityEffectStore ees = EffectHandler.getEffectStore(living);
		ees.addEffect(new EffectDamageFire(caster,living,magnitude,duration*20));
	}

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.damagefire"; }

	private final UVStore uv = new UVStore(0.1,0.2,0.3,0.4);
	@Override
	public UVStore getIconLocation(){ return uv; }

}
