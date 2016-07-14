package io.darkcraft.interop.thaumcraft.magic;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.PotionHelper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class WarpWard implements IComponent, IDurationComponent
{
	@Override
	public String id()
	{
		return "tc.warpward";
	}

	@Override
	public String getUnlocalisedName()
	{
		return "darkcraft.component.tc.warpward";
	}

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private UVStore uv = new UVStore(0.4,0.5, 0.5,0.6);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.illusion;
	}

	@Override
	public double getCost()
	{
		return 20;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(ent instanceof EntityPlayer)
		{
			EntityPlayer pl = (EntityPlayer) ent;
			if(duration == -1)
				duration = Integer.MAX_VALUE / 20;
			PotionHelper.applyPotion(pl, "potion.warpward", duration*20);
		}
	}

	@Override
	public void remove(EntityLivingBase ent)
	{
		ent.removePotionEffect(PotionHelper.getPotionIndex("potion.warpward"));
	}

	@Override
	public boolean applyToEnt(){ return true; }

	@Override
	public boolean applyToBlock(){ return false; }

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.mark.getProjectileTexture(); }

	@Override
	public UVStore getProjectileLocation(int frame){ return MagicalRegistry.mark.getProjectileLocation(frame); }

	@Override
	public int getMinDuration()
	{
		return 30;
	}

	@Override
	public int getMaxDuration()
	{
		return 3600;
	}

	@Override
	public double getCostDur(int duration, double oldCost)
	{
		return oldCost * Math.pow(duration, 1.01);
	}

}
