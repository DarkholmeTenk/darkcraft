package io.darkcraft.mod.common.magic.entities;

import io.darkcraft.darkcore.mod.abstracts.IEntityTransmittable;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.handlers.packets.EntityPacketHandler;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RaytraceHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IPositionProvider;
import io.darkcraft.mod.common.magic.entities.particles.BasicParticle;
import io.darkcraft.mod.common.magic.entities.particles.movement.Orbit;
import io.darkcraft.mod.common.magic.entities.particles.movement.Velocity;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

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
				double r = (rand.nextDouble() * 360);
				double x = MathHelper.sin(r);
				double z = MathHelper.cos(r);
				Velocity vel = new Velocity(motionX * 0.7, motionY * 0.7, motionZ * 0.7);
				vel.gravity = true;
				BasicParticle particle = new BasicParticle(worldObj, posX + x, posY, posZ + z, vel);
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
				Minecraft.getMinecraft().effectRenderer.addEffect(new BasicParticle(worldObj, posX + x, posY, posZ + z, new Orbit(this, 5)));
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
		if(ticksExisted != lastAge)
		{
			lastAge = ticksExisted;
			return lastPosition = new SimpleDoubleCoordStore(this);
		}
		return lastPosition;
	}

}
