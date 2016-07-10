package io.darkcraft.mod.common.magic.systems.component.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.TeleportHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDescriptiveMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

public class Recall implements IComponent, IDescriptiveMagnitudeComponent
{
	public final boolean crossDimensional;
	public Recall(boolean cd)
	{
		crossDimensional = cd;
	}

	@Override
	public String id(){ return crossDimensional? "recall.cd" : "recall"; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component." +id(); }

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uvN = new UVStore(0.4,0.5,0.2,0.3);
	private final UVStore uvC = new UVStore(0.5,0.6,0.2,0.3);
	@Override
	public UVStore getIconLocation(){ return crossDimensional ? uvC : uvN; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.conjuration; }

	@Override
	public double getCost()
	{
		return crossDimensional ? 300 : 65;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		EntityLivingBase e = (EntityLivingBase) ent;
		if(caster instanceof EntityCaster)
		{
			EntityCaster ec = (EntityCaster) caster;
			if(!(MagicConfig.recallOthers || Helper.isCaster(ec, e))) return;
			NBTTagCompound nbt = ec.getExtraData();
			if(nbt.hasKey("markLoc" + magnitude))
			{
				SimpleDoubleCoordStore markLoc = SimpleDoubleCoordStore.readFromNBT(nbt.getCompoundTag("markLoc"+magnitude));
				if(crossDimensional || (markLoc.world == WorldHelper.getWorldID(e)))
				{
					TeleportHelper.teleportEntity(e, markLoc);
				}
				else
				{
					Helper.playFizzleNoise(new SimpleDoubleCoordStore(e));
				}
			}
			else
			{
				Helper.playFizzleNoise(new SimpleDoubleCoordStore(e));
			}
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

	@Override
	public int getMinMagnitude(){ return 0; }

	@Override
	public int getMaxMagnitude(){ return 2; }

	@Override
	public double getCostMag(int magnitude, double oldCost){ return oldCost; }

	@Override
	public void getDescription(List<String> strings, int magnitude)
	{
	}

}
