package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.registries.MagicConfig;

import java.lang.ref.WeakReference;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityCaster implements ICaster
{
	private final WeakReference<EntityLivingBase> caster;

	public EntityCaster(EntityLivingBase ent)
	{
		caster = new WeakReference(ent);
	}

	public EntityLivingBase getCaster()
	{
		return caster.get();
	}

	@Override
	public double getMana()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxMana()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean useMana(double amount, boolean sim)
	{
		return true;
	}

	private double height()
	{
		EntityLivingBase ent = caster.get();
		if(ent == null) return 0;
		if(ent instanceof EntityPlayer)
			return ((EntityPlayer)ent).eyeHeight;
		else
			return ent.height / 2;
	}

	@Override
	public SimpleDoubleCoordStore getSpellCreationPos()
	{
		EntityLivingBase ent = caster.get();
		if(ent == null) return null;
		double dist = 1;
		double pitchRads = Math.toRadians(ent.rotationPitch + 90);
		double yawRads = Math.toRadians(ent.rotationYaw + 90);
		double hDist = dist * Math.sin(pitchRads);
		double posY = ent.posY + height() + (dist * Math.cos(pitchRads));
		double posX = ent.posX + (hDist * Math.cos(yawRads));
		double posZ = ent.posZ + (hDist * Math.sin(yawRads));
		return new SimpleDoubleCoordStore(ent.worldObj, posX, posY, posZ);
	}

	@Override
	public void setVelocity(EntitySpellProjectile sp)
	{
		EntityLivingBase ent = caster.get();
		if(ent == null) return;
		double dX = sp.posX - ent.posX;
		double dY = sp.posY - (ent.posY+height());
		double dZ = sp.posZ - ent.posZ;
		double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));
		double mult = MagicConfig.projectileSpeed / dist;
		dX *= mult;
		dY *= mult;
		dZ *= mult;
		sp.setVelocity(dX, dY, dZ);
		sp.velocityChanged = true;
	}
}
