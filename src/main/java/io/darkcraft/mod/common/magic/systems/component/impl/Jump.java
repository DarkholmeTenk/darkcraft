package io.darkcraft.mod.common.magic.systems.component.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

import skillapi.api.implement.ISkill;

public class Jump implements IComponent, IMagnitudeComponent
{
	@Override
	public String id()
	{
		return "jump";
	}

	@Override
	public ISkill getMainSkill()
	{
		return SkillRegistry.alteration;
	}

	@Override
	public double getCost()
	{
		return 25;
	}

	@Override
	public int getMinMagnitude()
	{
		return 1;
	}

	@Override
	public int getMaxMagnitude()
	{
		return 20;
	}

	@Override
	public double getCostMag(int magnitude, double oldCost)
	{
		return oldCost * ((magnitude * 2) - 1);
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity entity, int magnitude, int duration, int config)
	{
		if(!(entity instanceof EntityLivingBase))return;
		EntityLivingBase ent = (EntityLivingBase) entity;
		ent.motionY += Math.pow(magnitude/1.7,0.5);
		if(ent.isSprinting())
		{
			Vec3 lv = ent.getLookVec();
			//System.out.println("Motion: " + lv.xCoord + "," + lv.zCoord);
			ent.motionX += lv.xCoord * (0.5 + (magnitude / 10.0));
			ent.motionZ += lv.zCoord * (0.5 + (magnitude / 10.0));
		}
		ent.velocityChanged = true;
		if(ServerHelper.isServer(ent))
			DarkcraftMod.particle.createJumpParticles(new SimpleDoubleCoordStore(ent), Vec3.createVectorHelper(ent.motionX, ent.motionY, ent.motionZ));
	}

	@Override
	public boolean applyToEnt(){return true;}

	@Override
	public boolean applyToBlock(){return false;}

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.2,0.3,0,0.1)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

	@Override
	public String getUnlocalisedName(){return "darkcraft.component.jump";}

	@Override
	public ResourceLocation getIcon(){return MagicalRegistry.componentTex;}

	private final UVStore uv = new UVStore(0.6,0.7,0.0,0.1);
	@Override
	public UVStore getIconLocation(){return uv;}
}
