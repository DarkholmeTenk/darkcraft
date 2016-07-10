package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RaytraceHelper;
import io.darkcraft.darkcore.mod.helpers.TeleportHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import skillapi.api.implement.ISkill;

public class Blink implements IComponent, IMagnitudeComponent
{
	@Override
	public String id(){ return "blink"; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.blink"; }

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uv = new UVStore(0.3,0.4,0.2,0.3);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.conjuration; }

	@Override
	public double getCost()
	{
		return 15;
	}

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration, int config){}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration, int config)
	{
		if(!(ent instanceof EntityLivingBase)) return;
		MovingObjectPosition mop = RaytraceHelper.rayTrace(ent, magnitude, false, EntityLivingBase.class);
		SimpleDoubleCoordStore newPos = null;
		if((mop == null) || ((mop.typeOfHit != MovingObjectType.BLOCK) && (mop.entityHit == null)))
		{
			Vec3 end = RaytraceHelper.getEndPosition(ent, magnitude);
			newPos = new SimpleDoubleCoordStore(ent.worldObj, end.xCoord, end.yCoord, end.zCoord);
			if(newPos.getWorldObj().isAirBlock(newPos.iX, newPos.iY-1, newPos.iZ))
				newPos = newPos.translate(0, -1, 0);
		}
		else
		{
			if(mop.typeOfHit == MovingObjectType.BLOCK)
			{
				ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[mop.sideHit];
				SimpleCoordStore scs = new SimpleCoordStore(ent.worldObj, mop.blockX,mop.blockY,mop.blockZ).getNearby(d);
				if(WorldHelper.softBlock(scs.getWorldObj(), scs.x, scs.y - 1, scs.z))
					newPos = scs.getCenter().translate(0, -1.5, 0);
				else
					newPos = scs.getCenter().translate(0, -0.5, 0);
			}
			else
				newPos = new SimpleDoubleCoordStore(mop.entityHit);
		}
		if(newPos == null) return;
		TeleportHelper.teleportEntity(ent, newPos);
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
	public int getMinMagnitude(){ return 5; }

	@Override
	public int getMaxMagnitude(){ return 45; }

	@Override
	public double getCostMag(int magnitude, double oldCost){ return oldCost * magnitude; }

}
