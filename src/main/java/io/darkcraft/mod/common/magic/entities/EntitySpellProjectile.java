package io.darkcraft.mod.common.magic.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import io.darkcraft.darkcore.mod.abstracts.IEntityTransmittable;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.handlers.packets.EntityPacketHandler;
import io.darkcraft.darkcore.mod.helpers.RaytraceHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IPositionProvider;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

public class EntitySpellProjectile extends Entity implements IEntityTransmittable, IPositionProvider
{
	public Spell spell;
	public ICaster caster;

	public EntitySpellProjectile(World w)
	{
		super(w);
	}

	public EntitySpellProjectile(ICaster _caster, Spell _spell, SimpleDoubleCoordStore dcs)
	{
		super(dcs.getWorldObj());
		caster = _caster;
		spell = _spell;
		posX = dcs.x;
		posY = dcs.y;
		posZ = dcs.z;
	}

	@Override
	protected void entityInit()
	{
	}

	private void move()
	{
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
	}

	private AxisAlignedBB getAABB()
	{
		double mx = Math.min(posX, prevPosX);
		double my = Math.min(posY, prevPosY);
		double mz = Math.min(posZ, prevPosZ);
		double mX = Math.max(posX, prevPosX);
		double mY = Math.max(posY, prevPosY);
		double mZ = Math.max(posZ, prevPosZ);
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(mx,my,mz,mX,mY,mZ).expand(1, 1, 1);
		return aabb;
	}

	private MovingObjectPosition blockCasterHitCheck(MovingObjectPosition mop)
	{
		if(mop == null) return null;
		if(!(caster instanceof BlockCaster)) return mop;
		if(mop.entityHit != null) return mop;
		BlockCaster bc = (BlockCaster) caster;
		SimpleCoordStore scs = new SimpleCoordStore(worldObj, mop);
		if(scs.equals(bc.blockPos)) return null;
		return mop;
	}

	private void hitCheck()
	{
		if(ServerHelper.isClient())return;
		Entity c = caster instanceof EntityCaster ? ((EntityCaster)caster).getEntity() : null;
		MovingObjectPosition mop = RaytraceHelper.rayTrace(this, false, EntityLivingBase.class, true, spell.affectEntities, c);
		mop = blockCasterHitCheck(mop);
		if(mop != null)
		{
			if(mop.entityHit != null)
				spell.apply(caster, mop.entityHit);
			else
				spell.apply(caster, new SimpleCoordStore(worldObj, mop), mop.sideHit);
			setDead();
		}
	}

	@Override
	public void onEntityUpdate()
    {
		if(ticksExisted == 1)
			EntityPacketHandler.syncEntity(this);
		super.onEntityUpdate();
		hitCheck();
		move();
		if(ticksExisted > 500)
			setDead();
		if(ServerHelper.isClient())
		{
			if(Math.random() < 0.1)
			{
				DarkcraftMod.particle.createProjectileParticle(this);
			}
		}
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		spell = nbt.hasKey("spell") ? Spell.readFromNBT(nbt.getCompoundTag("spell")) : null;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		if(spell != null)
		{
			NBTTagCompound spellNBT = new NBTTagCompound();
			spell.writeToNBT(spellNBT);
			nbt.setTag("spell", spellNBT);
		}
	}

	@Override
	public void writeToNBTTransmittable(NBTTagCompound nbt)
	{
		//writeToNBT(nbt);
		writeEntityToNBT(nbt);
	}

	@Override
	public void readFromNBTTransmittable(NBTTagCompound nbt)
	{
		//readFromNBT(nbt);
		readEntityFromNBT(nbt);
	}

	private int lastAge = -1;
	private SimpleDoubleCoordStore lastPosition = null;
	@Override
	public SimpleDoubleCoordStore getPosition()
	{
		if(isDead)
			return null;
		if(ticksExisted != lastAge)
		{
			lastAge = ticksExisted;
			return lastPosition = new SimpleDoubleCoordStore(this);
		}
		return lastPosition;
	}

}
