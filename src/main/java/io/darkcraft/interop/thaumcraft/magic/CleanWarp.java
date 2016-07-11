package io.darkcraft.interop.thaumcraft.magic;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDescriptiveMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;
import thaumcraft.api.ThaumcraftApiHelper;

public class CleanWarp implements IComponent, IDescriptiveMagnitudeComponent
{

	@Override
	public String id()
	{
		return "tc.cleanwarp";
	}

	@Override
	public String getUnlocalisedName()
	{
		return "darkcraft.component.tc.cleanwarp";
	}

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private UVStore uv = new UVStore(0.4,0.5, 0.4,0.5);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.mysticism;
	}

	@Override
	public double getCost()
	{
		return 5000;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(ent instanceof EntityPlayer)
		{
			EntityPlayer pl = (EntityPlayer)ent;
			ThaumcraftApiHelper.addWarpToPlayer(pl, -10000, true);
			if(magnitude == 2)
				ThaumcraftApiHelper.addStickyWarpToPlayer(pl, -10000);
		}
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
	public int getMinMagnitude(){ return 1; }

	@Override
	public int getMaxMagnitude(){ return 2; }

	@Override
	public double getCostMag(int magnitude, double oldCost)
	{
		switch(magnitude)
		{
			case 2: return oldCost * 3;
			default: return oldCost;
		}
	}

	@Override
	public void getDescription(List<String> strings, int magnitude)
	{
		switch(magnitude)
		{
			case 2: strings.add("dc.tc.cleanwarp.2"); return;
			default: strings.add("dc.tc.cleanwarp.1"); return;
		}
	}

}
